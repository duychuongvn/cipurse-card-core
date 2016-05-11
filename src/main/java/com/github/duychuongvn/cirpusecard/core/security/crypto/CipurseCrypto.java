package com.github.duychuongvn.cirpusecard.core.security.crypto;

import com.github.duychuongvn.cirpusecard.core.constant.SwEnum;
import com.github.duychuongvn.cirpusecard.core.exception.Iso7816Exception;
import com.github.duychuongvn.cirpusecard.core.iso7816.ISO7816;
import org.osptalliance.cipurse.CipurseException;
import org.osptalliance.cipurse.IAes;
import org.osptalliance.cipurse.ILogger;
import org.osptalliance.cipurse.Utility;
import org.osptalliance.cipurse.Utility.eCaseType;

import java.util.Arrays;
import java.util.Random;

/**
 * Defines CIPURSE Card side crypto processing class.
 */
public class CipurseCrypto implements ISO7816 {

    private byte[] frameKeyi;

    private byte[] frameKeyiPlus1;


    //<summary>
    //Random challenge of PICC
    //</summary>
    private byte[] RP;

    //<summary>
    //PICC Random number different from Rp
    //</summary>
    private byte[] rP;

    //<summary>
    //Terminal challenge
    //</summary>
    private byte[] RT;

    //<summary>
    //Terminal Random number different from RT
    //</summary>
    private byte[] rT;

    //<summary>
    //OSPT Session key derived rp, Rp, rT, RT
    //</summary>
    private byte[] k0;

    private ILogger logger = null;
    private IAes Aes = null;

    public CipurseCrypto(IAes Aes, ILogger logger) {
        try {
            this.logger = logger;
            this.Aes = Aes;
        } catch (Exception ex) {
            logger.log(ex.getMessage());
            throw new Iso7816Exception(SwEnum.SW_UNKNOWN);
        }
    }

    /**
     * Gives current session key
     *
     * @return session key
     */
    public byte[] getSessionKey() {
        return frameKeyi;
    }

    /**
     * Sets current session key
     *
     * @param sessionKey session key
     */
    public void setSessionKey(byte[] sessionKey) {
        frameKeyi = sessionKey;
    }

    /**
     * <p>
     * Modify the given APDU a, according to SMI, Secure Messaging
     * Indicator. Also generate next frame key.
     * </p>
     *
     * @param plainCommand - Plain text command
     * @param SMI          - SMI byte
     * @return byte[] - Modified SM command
     * @throws CipurseException In case of any error while processing
     */
    public byte[] wrapCommand(byte[] plainCommand, byte SMI) throws CipurseException {

        // process according to SMI
        int smi = SMI & CipurseConstant.BITMAP_SMI_FOR_RESPONSE;
        byte[] wrappedCommand;
        switch (smi) {
            case CipurseConstant.SM_RESPONSE_MACED:
                wrappedCommand = getMACedCommand(plainCommand, SMI);
                break;

            case CipurseConstant.SM_RESPONSE_ENCED:
                wrappedCommand = getENCedCommand(plainCommand, SMI);
                break;

            case CipurseConstant.SM_COMMAND_RESPONSE_PLAIN:
                wrappedCommand = getPlainSMCommand(plainCommand, SMI);
                break;

            default:
                throw new CipurseException(CipurseConstant.INVALID_SMI);
        }
        return wrappedCommand;
    }

    /**
     * <p>
     * Unwrap the given SM-Command. Also generate next frame key.
     * </p>
     *
     * @param smCommand - SM command
     * @param SMI       - SMI byte
     * @return byte[] - unwrapped command
     * @throws CipurseException In case of any error while processing
     */
    public byte[] unwrapCommand(byte[] smCommand, byte SMI) {
        // process according to SMI
        byte[] unwrappedCommand = null;
        switch (SMI & CipurseConstant.BITMAP_SMI_COMMAND) {
            case CipurseConstant.SM_COMMAND_MACED:
                unwrappedCommand = unwrapMACedCommand(smCommand);
                break;

            case CipurseConstant.SM_COMMAND_ENCED:
                unwrappedCommand = unwrapENCedCommand(smCommand);
                break;

            case CipurseConstant.SM_COMMAND_RESPONSE_PLAIN:
                unwrappedCommand = unwrapPlainSMCommand(smCommand);
                break;

            default:
                throw new Iso7816Exception(SwEnum.SW_SECURE_MESSAGING_NOT_SUPPORTED);
        }

        return unwrappedCommand;
    }

    public byte[] generateCT(byte[] RT) throws CipurseException {
        return this.Aes.aesEncrypt(this.k0, this.RT);
    }

    /**
     * <p>
     * Generate session k0 as specified in the OSPT Crypto Doc, section 5.2
     * </p>
     *
     * @param kid - base key kid
     * @param RP1 - PICC random challenge
     * @param rP1 - PICC random string
     * @param RT1 - Terminal random challenge
     * @param rT1 - Terminal random string
     * @return byte[] - Cp - PICC cryptogram
     * @throws CipurseException In case of any error while processing
     */
    public byte[] generateK0AndGetCp(byte[] kid, byte[] RP1, byte[] rP1,
                                     byte[] RT1, byte[] rT1) throws CipurseException {
        byte[] temp1, temp2;

        if ((kid.length != CipurseConstant.AES_BLOCK_LENGTH)
                || (RP1.length != CipurseConstant.AES_BLOCK_LENGTH)
                || (RT1.length != CipurseConstant.AES_BLOCK_LENGTH)
                || (rP1.length != CipurseConstant.CIPURSE_SECURITY_PARAM_N)
                || (rT1.length != CipurseConstant.CIPURSE_SECURITY_PARAM_N)) {
            throw new CipurseException(CipurseConstant.INVALID_PARAMS);
        }

        this.rP = rP1;
        this.RP = RP1;
        this.RT = RT1;
        this.rT = rT1;

        // session key derivation function
        // kP := NLM(EXT(kID), rP)
        // k0 := AES(key=PAD2(kP) XOR PAD(rT),kID) XOR kID
        temp1 = extFunction(kid, CipurseConstant.CIPURSE_SECURITY_PARAM_N);
        byte[] kp = computeNLM(temp1, rP);

        temp1 = pad2(kp);
        temp2 = pad(rT);
        temp1 = xor(temp1, temp2);

        // session key K0
        k0 = Aes.aesEncrypt(temp1, kid);
        k0 = xor(k0, kid);

        // first frame key k1, function to calculate k1,
        // k1 := AES(key = RP; k0 XOR RT) XOR (k0 XOR RT)
        temp1 = xor(k0, RT);
        byte[] temp3 = Aes.aesEncrypt(RP, temp1);
        frameKeyi = xor(temp3, temp1);

        // function to caluclate cP := AES(key=k0, RP).
        // terminal response
        byte[] cP = Aes.aesEncrypt(k0, RP);

        return cP;
    }

    /**
     * <p>
     * To get Random number
     * </p>
     *
     * @param size - byte array size
     * @return byte[] - random byte array
     */
    public byte[] getRandom(int size) {
        byte[] random = new byte[size];

        for (int i = 0; i < size; i++) {
            random[i] = (byte) (((new Random()).nextInt() % 200) & 0x0FF);
        }

        return random;
    }

    /**
     * <p>
     * Verify the PICC response
     * </p>
     *
     * @param cT - Terminal cryptogram
     * @return status of verification
     * @throws CipurseException In case of any error while processing
     */
    public boolean verifyPICCResponse(byte[] cT) throws CipurseException {
        //function to caluclate c'T := AES(key=k0, RT)
        byte[] cTDash = Aes.aesEncrypt(k0, RT);
        return Arrays.equals(cT, cTDash);
    }

    /**
     * <p>
     * Generate the plain SM-Command for the given Command
     * </p>
     *
     * @param command - plain command
     * @param SMI     - SMI byte
     * @return byte[] Plain SM-Command
     */
    public byte[] getPlainSMCommand(byte[] command, byte SMI) {
        //TODO: check valid command and rename method, variable
        // Original command APDU:
        // - CLA - INS - P1 - P2 - {Lc} - {DATA} - {Le}
        // Transferred command SM-APDU:
        // - CLA' - INS - P1 - P2 - Lc' - SMI - {DATA} - {Le} - {Le'}
        // The Le'field is not present for the case where in the
        // original APDU the Le field is not present and
        // the PCD requests the PICC response in SM_PLAIN.
        //		byte[] smCommand = getOSPTModifiedCommand(command, SMI);
        // Calculate virtual MAC and generate new frame key
        byte[] smCommand = new byte[command.length];
        System.arraycopy(command, 0, smCommand, 0, command.length);
        byte[] dataPadded = Padding.schemeISO9797M2(smCommand, CipurseConstant.AES_BLOCK_LENGTH);
        generateMAC(dataPadded);
        return smCommand;
    }

    /**
     * <p>
     * Calculate the MAC for the command
     * </p>
     *
     * @param command - plain command
     * @param SMI     - SMI byte
     * @return byte[] - MACed command
     */
    public byte[] getMACedCommand(byte[] command, byte SMI) {

        // TODO: check command and rename
        //		Original response APDU:
        //		• {DATA} - SW1 - SW2
        //		Transferred MAC’ed response SM-APDU:
        //		• {DATA} - MAC - SW1 - SW2
        byte[] smMacCommand = new byte[command.length + CipurseConstant.OSPT_MAC_LENGTH];
        byte[] smMacData = new byte[command.length];
        System.arraycopy(command, 0, smMacData, 0, command.length);
        System.arraycopy(command, 0, smMacCommand, 0, command.length - CipurseConstant.OSPT_RESPONSE_STATUS_LENGTH);
        byte[] dataPadded = Padding.schemeISO9797M2(smMacData, CipurseConstant.AES_BLOCK_LENGTH);
        // generate MAc and compute next frame key
        byte[] mac = generateMAC(dataPadded);
        System.arraycopy(mac, 0, smMacCommand, command.length - CipurseConstant.OSPT_RESPONSE_STATUS_LENGTH, CipurseConstant.OSPT_MAC_LENGTH);
        System.arraycopy(command, command.length - CipurseConstant.OSPT_RESPONSE_STATUS_LENGTH,
                smMacCommand, smMacCommand.length - CipurseConstant.OSPT_RESPONSE_STATUS_LENGTH, CipurseConstant.OSPT_RESPONSE_STATUS_LENGTH);
        return smMacCommand;
    }

    /**
     * <p>
     * Encrypt the command
     * </p>
     *
     * @param command - plain text command
     * @param SMI     - SMI byte
     * @return byte[] - encrypted command
     */
    private byte[] getENCedCommand(byte[] command, byte SMI) {
        //TODO: check command and rename variable

//		{DATA} - SW1 - SW2
//		Transferred ENC’ed response SM-APDU:
//		• n*CRYPTOGRAM - SW1 - SW2
//		The n CRYPTOGRAM(s) of 16 byte length are calculated from 16-byte blocks of the following composition of
//		elements:
//		• {DATA} - MIC - SW1 - SW2 - padding
        byte[] orgLe = null;
        byte[] orgCommandData = null;

        // prepare data for MIC calculation
        // - CLA' - INS - P1 - P2 - Lc' - SMI - {DATA} - {Le}
        byte[] dataForMIC = new byte[command.length];
        System.arraycopy(command, 0, dataForMIC, 0, dataForMIC.length);
        byte[] mic = computeMIC(dataForMIC);
        logger.log(ILogger.INFO_MESSAGE, "MIC", mic);

        // Original command + MIC
        byte[] smCommand = new byte[command.length + CipurseConstant.MIC_LENGH];
        System.arraycopy(command, 0, smCommand, 0, command.length - 2);
        System.arraycopy(mic, 0, smCommand, command.length - 2, 4);
        System.arraycopy(command, command.length - 2, smCommand, smCommand.length - 2, 2);
        byte[] nCryptogramPadded = Padding.schemeISO9797M2(smCommand, CipurseConstant.AES_BLOCK_LENGTH);
        byte[] nCryptogramCipher = generateCipher(nCryptogramPadded, true);

        byte[] response = new byte[nCryptogramCipher.length + 2];
        System.arraycopy(nCryptogramCipher, 0, response, 0, nCryptogramCipher.length);
        System.arraycopy(command, command.length - 2, response, response.length - 2, 2);
        return response;
    }

    /**
     * <p>
     * Unwrap the plain SM-response command
     * </p>
     *
     * @param smCommand - Plain SM command
     * @return byte[] - Unwrapped command
     */
    private byte[] unwrapPlainSMCommand(byte[] smCommand) {
        // calculate frame key
        byte[] dataPadded = Padding.schemeISO9797M2(smCommand,
                CipurseConstant.AES_BLOCK_LENGTH);

        // generate virtual MAC and generate next frame key
        generateMAC(dataPadded);
        return smCommand;
    }

    /**
     * <p>
     * Unwrap the MACed command
     * </p>
     *
     * @param smCommand - MACed SM command
     * @return byte[] - Unwrapped command
     * @throws CipurseException
     */
    private byte[] unwrapMACedCommand(byte[] smCommand) {

        // TODO: check valid command and rename

        byte lc = smCommand[OFFSET_LC];
        byte[] command = new byte[5 + lc];

        System.arraycopy(smCommand, 0, command, 0, command.length);
        //
        //        Original command APDU:
        //        • CLA - INS - P1 - P2 - {Lc} - {DATA} - {Le}
        //        Transferred MAC’ed command SM-APDU:
        //        • CLA’ - INS - P1 - P2 - Lc’ - SMI - {DATA} - {Le} - MAC - {Le’}
        //        Note: The value for Lc’ includes SMI, {DATA}, {Le}, and MAC,
        //          hence the limit for the original Lc is 246 (with Le not present) resp.
        //        245 (with Le present).
        //        The MAC is calculated over one or multiple 16-byte blocks of the following composition of elements:
        //        • CLA’ - INS - P1 - P2 - Lc’ - SMI - {DATA} - {Le} - padding
        //
        byte[] dataForMAC = new byte[command.length - CipurseConstant.OSPT_MAC_LENGTH];
        System.arraycopy(command, 0, dataForMAC, 0, dataForMAC.length);
        byte[] dataPadded = Padding.schemeISO9797M2(dataForMAC, CipurseConstant.AES_BLOCK_LENGTH);
        byte[] cardMac = this.generateMAC(dataPadded);
        byte[] hostMac = new byte[CipurseConstant.OSPT_MAC_LENGTH];
        System.arraycopy(command, command.length - CipurseConstant.OSPT_MAC_LENGTH, hostMac, 0, CipurseConstant.OSPT_MAC_LENGTH);
        if (!Arrays.equals(cardMac, hostMac)) {
            throw new Iso7816Exception(SwEnum.SW_SECURITY_STATUS_NOT_SATISFIED);
        }
        return dataForMAC;

    }

    private boolean isLePresent(byte[] command) {
        byte INS = command[OFFSET_INS];
        boolean isReadCommand = false;
        // READ INSTRUCTION
        if ((INS & 0xff) == 0xB0) {
            isReadCommand = true;
        }
        return isReadCommand;
    }

    /**
     * <p>
     * Unwrap the encrypted reponse
     * </p>
     *
     * @param smCommand - ENCed SM command
     * @return byte[] - Unwrapped command
     * @throws CipurseException
     */
    private byte[] unwrapENCedCommand(byte[] smCommand) {

        // TODO: check valid command and rename
        //		Original command APDU:
        //		• CLA - INS - P1 - P2 - {Lc} - {DATA} - {Le}
        //		Transferred ENC’ed command SM-APDU:
        //		• CLA’ - INS - P1 - P2 - Lc’ - SMI - n*CRYPTOGRAM - {Le} - {Le’}

        short lc1 = (short) (smCommand[OFFSET_LC] & 0xFF);
        boolean isLePresent = isLePresent(smCommand);
        byte[] encryptedResp = new byte[lc1 - 1];
        byte le = 0x00;
        if (isLePresent) {
            encryptedResp = new byte[lc1 - 2];
            le = smCommand[encryptedResp.length + 6];
        }
        byte[] encryptedRequestTemp = new byte[encryptedResp.length];
        System.arraycopy(smCommand, 6, encryptedResp, 0, encryptedResp.length);

        System.arraycopy(encryptedResp, 0, encryptedRequestTemp, 0, encryptedResp.length);
        if ((encryptedResp.length % CipurseConstant.AES_BLOCK_LENGTH) != 0) {
            logger.log(ILogger.ERROR_MESSAGE, "Request not multi AES block");
            throw new Iso7816Exception(SwEnum.SW_SECURITY_STATUS_NOT_SATISFIED);
        }

        // CLA’ - INS - P1 - P2 - {DATA} - MIC - padding

        // decrypt response data
        byte[] clearResp = generateCipher(encryptedResp, false);
        logger.log(ILogger.INFO_MESSAGE, "Deciphered Data", clearResp);
        byte[] unpaddedClearResp = Padding.removeISO9797M2(clearResp);
        // data for MIC
        // CLA’ - INS - P1 - P2 - {DATA} - MIC - padding
        int dataForMicLen = unpaddedClearResp.length - 4 + 2;
        if (isLePresent) {
            dataForMicLen += 1;
        }
        byte[] dataForMIC = new byte[dataForMicLen];

        // copy command header
        System.arraycopy(smCommand, 0, dataForMIC, 0, 6);
        // copy data
        System.arraycopy(unpaddedClearResp, 4, dataForMIC, 6, dataForMIC.length - 6);

        if (isLePresent) {
            dataForMIC[dataForMicLen - 1] = le;
        }
        byte[] cardMIC = computeMIC(dataForMIC);
        byte[] hostMIC = new byte[CipurseConstant.MIC_LENGH];
        System.arraycopy(unpaddedClearResp, (unpaddedClearResp.length - 4), hostMIC, 0, CipurseConstant.MIC_LENGH);

        if (!Arrays.equals(cardMIC, hostMIC)) {
            logger.log(ILogger.ERROR_MESSAGE, "MIC verify failed");
            throw new Iso7816Exception(SwEnum.SW_SECURITY_STATUS_NOT_SATISFIED);
        }
        return dataForMIC;
    }


    /**
     * <p>
     * Generate OSPT MAC on the given input data.
     * Data should be already padded.
     * </p>
     *
     * @param dataMAC - padded input data for MAC
     * @return byte[] - generated MAC value
     * @throws CipurseException
     */
    private byte[] generateMAC(byte[] dataMAC) {
        /*
         * Calculation of Mi and ki+1: hx := ki , hx+1 := AES( key = hx ; Dx )
		 * XOR Dx , hx+2 := AES( key = hx+1 ; Dx+1 ) XOR Dx+1, hx+3 := AES( key =
		 * hx+2 ; Dx+2 ) XOR Dx+2, ... hy+1 := AES( key = hy ; Dy ) XOR Dy, ki+1 :=
		 * hy+1 M'i := AES( key = ki ; ki+1 ) XOR ki+1, Mi := m LS bits of M'i = (
		 * (M'i )0, (M'i )1, ..., (M'i )m-1)
		 */

        // data should be padded and block aligned to AES_BLOCK_LENGTH
        if ((dataMAC.length % CipurseConstant.AES_BLOCK_LENGTH) != 0) {
            logger.log(ILogger.ERROR_MESSAGE, "INVALID AES_BLOCK_LENGTH");
            throw new Iso7816Exception(SwEnum.SW_DATA_INVALID);
        }

        byte[] blockDx = new byte[CipurseConstant.AES_BLOCK_LENGTH];
        frameKeyiPlus1 = frameKeyi;
        try {
            for (int i = 0; i < dataMAC.length; i += CipurseConstant.AES_BLOCK_LENGTH) {
                System.arraycopy(dataMAC, i, blockDx, 0, CipurseConstant.AES_BLOCK_LENGTH);
                byte[] temp = Aes.aesEncrypt(frameKeyiPlus1, blockDx);
                frameKeyiPlus1 = xor(temp, blockDx);
            }

            logger.log(ILogger.INFO_MESSAGE, "Frame Key", frameKeyiPlus1);
            byte[] macBlock = xor(Aes.aesEncrypt(frameKeyi, frameKeyiPlus1), frameKeyiPlus1);
            System.arraycopy(frameKeyiPlus1, 0, frameKeyi, 0, CipurseConstant.AES_BLOCK_LENGTH);
            byte[] actualMAC = new byte[CipurseConstant.OSPT_MAC_LENGTH];
            System.arraycopy(macBlock, 0, actualMAC, 0, CipurseConstant.OSPT_MAC_LENGTH);

            return actualMAC;
        } catch (CipurseException cx) {
            logger.log(ILogger.ERROR_MESSAGE, cx.getMessage());
            throw new Iso7816Exception(SwEnum.SW_UNKNOWN);

        }
    }

    /**
     * <p>
     * Encrypt the given data using ciphering mechanism explained the OPST.
     * </p>
     *
     * @param dataENC   - data to be ciphered
     * @param isEncrypt - to encrypt or decrypt
     * @return byte[] - Ciphered Data
     */
    private byte[] generateCipher(byte[] dataENC, boolean isEncrypt) {
        /*
         * hx-1 := ki , hx := AES( key = hx-1 ; q) XOR q, Cx := AES( key = hx ;
		 * Dx ), hx+1 := AES( key = hx ; q ) XOR q, Cx+1 := AES( key = hx+1 ;
		 * Dx+1 ), ... hy := AES( key = hy-1 ; q ) XOR q, Cy := AES( key = hy ;
		 * Dy ), ki+1 := hy
		 */

        // data should be padded and block aligned to AES_BLOCK_LENGTH
        if ((dataENC.length % CipurseConstant.AES_BLOCK_LENGTH) != 0) {
            logger.log(ILogger.ERROR_MESSAGE, "INVALID_MAC_BLOCK");
            throw new Iso7816Exception(SwEnum.SW_DATA_INVALID);
        }

        byte[] blockDx = new byte[CipurseConstant.AES_BLOCK_LENGTH];
        byte[] ciphered = new byte[dataENC.length];

        frameKeyiPlus1 = frameKeyi;
        for (int i = 0; i < dataENC.length; i += CipurseConstant.AES_BLOCK_LENGTH) {
            try {
                byte[] hx = Aes.aesEncrypt(CipurseConstant.qConstant, frameKeyiPlus1);
                hx = xor(hx, frameKeyiPlus1);

                System.arraycopy(dataENC, i, blockDx, 0, CipurseConstant.AES_BLOCK_LENGTH);
                byte[] temp = null;
                if (isEncrypt) {
                    temp = Aes.aesEncrypt(hx, blockDx);
                } else {
                    temp = Aes.aesDecrypt(hx, blockDx);
                }
                System.arraycopy(temp, 0, ciphered, i, CipurseConstant.AES_BLOCK_LENGTH);
                System.arraycopy(hx, 0, frameKeyiPlus1, 0, CipurseConstant.AES_BLOCK_LENGTH);
            } catch (CipurseException cx) {
                logger.log(ILogger.ERROR_MESSAGE, cx.getMessage());
                throw new Iso7816Exception(SwEnum.SW_DATA_INVALID);
            }
        }

        logger.log(ILogger.INFO_MESSAGE, "Frame Key", frameKeyiPlus1);

        System.arraycopy(frameKeyiPlus1, 0, frameKeyi, 0, CipurseConstant.AES_BLOCK_LENGTH);
        return ciphered;
    }

    /**
     * <p>
     * Calculate the MIC on given data
     * </p>
     *
     * @param dataForMIC - MIC data
     * @return byte[] - MIC value
     */
    private byte[] computeMIC(byte[] dataForMIC) {
        int MIC_LENGH = 4;
        byte[] mic = new byte[4];

        byte[] paddedDataForMIC = null;

        // check if length of the input array is multiple of 4
        // else pad it with 00
        if ((dataForMIC.length % MIC_LENGH) != 0) {
            int toBePadded = MIC_LENGH - (dataForMIC.length % MIC_LENGH);
            paddedDataForMIC = new byte[dataForMIC.length + toBePadded];
            // remaining bytes are already zeros
            System.arraycopy(dataForMIC, 0, paddedDataForMIC, 0, dataForMIC.length);
        } else {
            paddedDataForMIC = new byte[dataForMIC.length];
            System.arraycopy(dataForMIC, 0, paddedDataForMIC, 0, paddedDataForMIC.length);
        }

        long crc1 = computeCRC(paddedDataForMIC);

        // swap bytes in paddedDataForMIC
        for (int i = 0; i <= (paddedDataForMIC.length - 4); i += 4) {
            byte temp1 = paddedDataForMIC[i];
            byte temp2 = paddedDataForMIC[i + 1];
            paddedDataForMIC[i] = paddedDataForMIC[i + 2];
            paddedDataForMIC[i + 1] = paddedDataForMIC[i + 3];
            paddedDataForMIC[i + 2] = temp1;
            paddedDataForMIC[i + 3] = temp2;
        }

        long crc2 = computeCRC(paddedDataForMIC);

        mic[0] = (byte) ((crc2 >> 8) & 0x00ff);
        mic[1] = (byte) (crc2 & 0x00ff);
        mic[2] = (byte) ((crc1 >> 8) & 0x00ff);
        mic[3] = (byte) (crc1 & 0x00ff);

        return mic;
    }

    /**
     * <p>
     * Calculate the CRC on given data
     * </p>
     *
     * @param inputData - CRC data
     * @return long - CRC value
     */
    private long computeCRC(byte[] inputData) {

        long initialCRC = 0x6363;
        long ch = 0;

        for (int i = 0; i < inputData.length; i++) {
            ch = (short) (inputData[i] & 0xFF);
            ch = (short) (ch ^ (short) ((initialCRC) & 0xFF));
            ch = (short) ((ch ^ (ch << 4)) & 0xFF);

            long first = (long) ((initialCRC >> 8) & 0x0FFFF);
            long second = (long) ((ch << 8) & 0x0FFFF);
            long third = (long) ((ch << 3) & 0x0FFFF);
            long four = (long) ((ch >> 4) & 0x0FFFF);

            initialCRC = (long) ((first ^ second ^ third ^ four) & 0xFFFF);
        }
        return initialCRC;
    }

    private byte[] getLeDashCommand(byte[] smCommand, eCaseType orgCaseType,
                                    byte SMI) {
        byte[] smPlainCommand = null;

        if ((orgCaseType == eCaseType.CASE_1)
                || (orgCaseType == eCaseType.CASE_3)) {
            if ((SMI & CipurseConstant.BITMAP_SMI_FOR_RESPONSE) != CipurseConstant.SM_COMMAND_RESPONSE_PLAIN) {
                smPlainCommand = new byte[smCommand.length + 1];
                System.arraycopy(smCommand, 0, smPlainCommand, 0, smCommand.length);
                smPlainCommand[smPlainCommand.length - 1] = CipurseConstant.LE_DASH;
            } else {
                smPlainCommand = smCommand;
            }
        } else {
            smPlainCommand = new byte[smCommand.length + 1];
            System.arraycopy(smCommand, 0, smPlainCommand, 0, smCommand.length);
            smPlainCommand[smPlainCommand.length - 1] = CipurseConstant.LE_DASH;
        }

        return smPlainCommand;
    }

    /**
     * <p>
     * Byte wise XOR the given two arrays
     * </p>
     *
     * @param firstArray  - Array One
     * @param secondArray - Array Two
     * @return byte[] - XORed byte array
     * @throws CipurseException - Invalid parameters
     */
    private byte[] xor(byte[] firstArray, byte[] secondArray) throws CipurseException {
        if (firstArray.length != secondArray.length) {
            throw new CipurseException(CipurseConstant.INVALID_PARAMS);
        }

        byte[] resultArray = new byte[firstArray.length];
        for (int i = 0; i < firstArray.length; i++) {
            resultArray[i] = (byte) (firstArray[i] ^ secondArray[i]);
        }
        return resultArray;

    }

    /**
     * <p>
     * Modify the given command as per OSPT SM-Command
     * </p>
     *
     * @param command - Command to be modified
     * @param SMI     - SM Indicator
     * @return byte[] - Modified Command
     * @throws CipurseException - if APDU case in invalid
     */
    private byte[] getOSPTModifiedCommand(byte[] command, byte SMI)
            throws CipurseException {

        eCaseType caseType = Utility.getCaseType(command);

        byte[] osptModifiedCmd = null;
        byte[] commandHeader = new byte[4];

        if (command.length >= CipurseConstant.OFFSET_LC) {
            System.arraycopy(command, 0, commandHeader, 0, 4);
        }

        switch (caseType) {
            case CASE_1:
                osptModifiedCmd = new byte[commandHeader.length + 2];
                System.arraycopy(commandHeader, 0, osptModifiedCmd, 0,
                        commandHeader.length);
                osptModifiedCmd[CipurseConstant.OFFSET_LC] = 0x01; // Lc
                osptModifiedCmd[CipurseConstant.OFFSET_CMD_DATA] = SMI; // Data
                break;

            case CASE_2:
                osptModifiedCmd = new byte[commandHeader.length + 3];
                System.arraycopy(commandHeader, 0, osptModifiedCmd, 0,
                        commandHeader.length);
                osptModifiedCmd[CipurseConstant.OFFSET_LC] = 0x02; // Lc
                osptModifiedCmd[CipurseConstant.OFFSET_CMD_DATA] = SMI; // Data
                osptModifiedCmd[CipurseConstant.OFFSET_CMD_DATA + 1] = command[CipurseConstant.OFFSET_LC]; // Le
                break;

            case CASE_3:
                osptModifiedCmd = new byte[(short) (commandHeader.length + 2
                        + (short) ((command[CipurseConstant.OFFSET_LC] & 0x00FF)))];
                System.arraycopy(commandHeader, 0, osptModifiedCmd, 0,
                        commandHeader.length);
                osptModifiedCmd[CipurseConstant.OFFSET_LC] = (byte) ((int) (command[CipurseConstant.OFFSET_LC]) + 0x01); // Lc
                osptModifiedCmd[CipurseConstant.OFFSET_CMD_DATA] = SMI; // Data
                System.arraycopy(command, CipurseConstant.OFFSET_CMD_DATA,
                        osptModifiedCmd, CipurseConstant.OFFSET_CMD_DATA + 1,
                        (short) ((command[CipurseConstant.OFFSET_LC] & 0x00FF)));
                break;

            case CASE_4:
                osptModifiedCmd = new byte[commandHeader.length + 3
                        + (short) ((command[CipurseConstant.OFFSET_LC] & 0x00FF))];
                System.arraycopy(commandHeader, 0, osptModifiedCmd, 0,
                        commandHeader.length);
                osptModifiedCmd[CipurseConstant.OFFSET_LC] = (byte) (command[4] + 0x02); // Lc
                osptModifiedCmd[CipurseConstant.OFFSET_CMD_DATA] = SMI; // Data
                System.arraycopy(command, CipurseConstant.OFFSET_CMD_DATA,
                        osptModifiedCmd, CipurseConstant.OFFSET_CMD_DATA + 1,
                        (short) ((command[CipurseConstant.OFFSET_LC] & 0x00FF)));
                osptModifiedCmd[osptModifiedCmd.length - 1] = command[command.length - 1]; // Le
                break;

            default:
                throw new CipurseException(CipurseConstant.INVALID_CASE);
        }

        osptModifiedCmd[0] = (byte) (osptModifiedCmd[0] | CipurseConstant.SM_BIT);
        return osptModifiedCmd;
    }

    /**
     * <p>
     * Compute Non Linear Mapping(NLM) of two 48 bit integers as specified in
     * OSPT Crypto Document Section 7.1.
     * </p>
     *
     * @param x - 48 bit integer 1 as byte array
     * @param y - 48 bit integer 2 as byte array
     * @return byte[] - NLM of x and y as a byte array
     */
    private byte[] computeNLM(byte[] x, byte[] y) {
        int shiftBitsBy = 40;
        int i;
        long x1 = 0, y1 = 0;

        for (i = 0; i < x.length; i++) {
            x1 |= ((long) (x[i] & 0x00FF) << (shiftBitsBy - (i * 8)));
        }

        for (i = 0; i < y.length; i++) {
            y1 |= ((long) (y[i] & 0x00FF) << (shiftBitsBy - (i * 8)));
        }

        long nlm = computeNLM(x1, y1);
        byte[] retNLM = new byte[6];
        for (i = 0; i < retNLM.length; i++) {
            retNLM[i] = (byte) (nlm >> (shiftBitsBy - (i * 8)));
        }

        return retNLM;
    }

    /**
     * <p>
     * Compute Non Linear Mapping(NLM) of two 48 bit integers as specified in
     * OSPT Crypto Document Section 7.1.
     * </p>
     *
     * @param x - 48 bit integer 1
     * @param y - 48 bit integer 2
     * @return long - NLM of x and y
     */
    private long computeNLM(long x, long y) {
        int i;
        long nlm = 0;
        final long constPolynomial = 0x35b088cce172L;

        for (i = 0; i < 48; i++) {

            nlm = shiftRight(nlm);
            if ((nlm & 1) == 1) {
                nlm = nlm ^ constPolynomial;
            }

            y = shiftRight(y);
            if ((y & 1) == 1) {
                nlm = nlm ^ x;
            }
        }

        return nlm;
    }

    /**
     * <p>
     * Shift right the long value by one bit. Out of 64 bits of long only 48 LS
     * bits are considered remaining bits to be ignored.
     * </p>
     *
     * @param ui48Bit - 48 bit integer
     * @return long - shifted 48 bit integer
     */
    private long shiftRight(long ui48Bit) {
        ui48Bit = ui48Bit << 1;
        if ((ui48Bit & 0x0001000000000000L) != 0) {
            ui48Bit = ui48Bit | 1;
            ui48Bit = ui48Bit & 0x0000ffffffffffffL;
        }
        return ui48Bit;
    }

    /**
     * <p>
     * OSPT specific padding of an array. Given array is pre-pended with zeros
     * </p>
     *
     * @param x - array to be padded
     * @return y - padded array
     * @throws CipurseException - Invalid parameters
     */
    private byte[] pad(byte[] x) throws CipurseException {
        if (x.length > CipurseConstant.AES_BLOCK_LENGTH) {
            throw new CipurseException(CipurseConstant.INVALID_PAD_LEN);
        }

        byte[] y = new byte[CipurseConstant.AES_BLOCK_LENGTH];
        System.arraycopy(x, 0, y, (y.length - x.length), x.length);

        return y;
    }

    /**
     * <p>
     * OSPT specific padding of an array. Given array is doubled and pre-pended
     * with zeros
     * </p>
     *
     * @param x - array to be padded
     * @return y - padded array
     * @throws CipurseException - Invalid parameters
     */
    private byte[] pad2(byte[] x) throws CipurseException {
        // length check
        if ((x.length * 2) > CipurseConstant.AES_BLOCK_LENGTH) {
            throw new CipurseException(CipurseConstant.INVALID_2PAD_LEN);
        }

        byte[] y = new byte[CipurseConstant.AES_BLOCK_LENGTH];
        System.arraycopy(x, 0, y, (CipurseConstant.AES_BLOCK_LENGTH - x.length),
                x.length);
        System.arraycopy(x, 0, y,
                (CipurseConstant.AES_BLOCK_LENGTH - (x.length * 2)), x.length);

        return y;
    }

    /**
     * <p>
     * Extract least significant N bytes from x
     * </p>
     *
     * @param x - input array
     * @param N - Number of bytes to extracted
     * @return byte[] - extracted array
     * @throws CipurseException - Invalid parameters
     */
    private byte[] extFunction(byte[] x, int N) throws CipurseException {
        if (x.length < N) {
            throw new CipurseException(CipurseConstant.INVALID_PARAMS);
        }

        byte[] y = new byte[N];
        System.arraycopy(x, (x.length - N), y, 0, N);
        return y;
    }

}

