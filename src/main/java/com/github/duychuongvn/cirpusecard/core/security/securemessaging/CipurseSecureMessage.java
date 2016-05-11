package com.github.duychuongvn.cirpusecard.core.security.securemessaging;

import com.github.duychuongvn.cirpusecard.core.command.CommandApdu;
import com.github.duychuongvn.cirpusecard.core.constant.SwEnum;
import com.github.duychuongvn.cirpusecard.core.exception.Iso7816Exception;
import com.github.duychuongvn.cirpusecard.core.iso7816.ISO7816;
import com.github.duychuongvn.cirpusecard.core.security.crypto.CipurseCrypto;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.osptalliance.cipurse.*;
import org.osptalliance.cipurse.securemessaging.ICipurseSMKey;

import java.util.Arrays;

/**
 * Created by caoky on 1/9/2016.
 */
public class CipurseSecureMessage implements ISO7816 {
    private static short CHALLENGES_MAX_LENGTH = 255;

    private static CipurseSecureMessage cipurseSecureMessage;
    private CipurseCrypto cipurseCrypto = null;
    private ILogger logger = null;
    private IAes Aes = null;

    private byte[] mutualAuthHeader = new byte[]{(byte) 0, (byte) -126, (byte) 0, (byte) 0};
    private final byte[] getChallengeCommand = new byte[]{(byte) 0, (byte) -124, (byte) 0, (byte) 0, (byte) 22};
    private byte[][] keySet = null;
    private ICipurseSMKey samSmKey;
    private static byte[] nullVector = new byte[16];
    private byte[] selectedKey;
    private short _0 = 0;
    private byte[] rP;
    private byte[] RP;
    private byte[] mutualAuthCmd = null;
    private byte[] Ct;
    private byte SMI;
    private boolean isSecuredMessage;

    public static CipurseSecureMessage getInstance(IAes aes, ILogger logger) throws CipurseException {
        if (cipurseSecureMessage == null) {
            cipurseSecureMessage = new CipurseSecureMessage(aes, logger);
        }
        return cipurseSecureMessage;
    }


    CipurseSecureMessage(IAes Aes, ILogger logger) {
        this.logger = logger;
        this.Aes = Aes;
        this.cipurseCrypto = new CipurseCrypto(Aes, logger);
    }

    public void setKeyValues(byte[][] keySet) {
        this.keySet = keySet;
    }

    public byte[] wrapCommand(byte[] plainCommand, byte SMI) throws CipurseException {
        this.SMI = SMI;
        this.isSecuredMessage = true;
        return this.samSmKey != null ? this.samSmKey.getCipurseSM().wrapCommand(plainCommand, SMI) : this.cipurseCrypto.wrapCommand(plainCommand, SMI);
    }

    public byte[] unWrapCommand(byte[] smCommand, byte SMI) throws CipurseException {
        return this.samSmKey != null ? this.samSmKey.getCipurseSM().unWrapCommand(smCommand, SMI) : this.cipurseCrypto.unwrapCommand(smCommand, SMI);
    }

    public byte[] getKVV(byte[] forKey) throws CipurseException {
        if (forKey.length % 16 != 0) {
            throw new CipurseException("Not a valid key length");
        } else {
            byte[] cipherText = this.Aes.aesEncrypt(forKey, nullVector);
            byte[] kvv = new byte[3];
            System.arraycopy(cipherText, 0, kvv, 0, 3);
            return kvv;
        }
    }

    public byte[] encryptText(byte[] keyValue, byte[] textToBeEncrypted) throws CipurseException {
        return this.Aes.aesEncrypt(keyValue, textToBeEncrypted);
    }

    public byte[] encryptText(byte keyNum, byte[] textToBeEncrypted) throws CipurseException {
        if (this.keySet != null && this.keySet.length > keyNum) {
            return this.Aes.aesEncrypt(this.keySet[keyNum], textToBeEncrypted);
        } else {
            throw new CipurseException("Key value is not initialized");
        }
    }

    public byte[] decryptText(byte[] keyValue, byte[] textToBeDecrypted) throws CipurseException {
        return this.Aes.aesDecrypt(keyValue, textToBeDecrypted);
    }

    public byte[] decryptText(byte keyNum, byte[] textToBeDecrypted) throws CipurseException {
        if (this.keySet != null && this.keySet.length > keyNum) {
            return this.Aes.aesDecrypt(this.keySet[keyNum], textToBeDecrypted);
        } else {
            throw new CipurseException("Key value is not initialized");
        }
    }

    public byte[] encryptText(byte keyAlgorithm, byte[] keyValue, byte[] textToBeEncrypted, CryptoParameters params) throws CipurseException {
        if (this.Aes instanceof ICryptoEngine) {
            ICryptoEngine padAlgo1 = (ICryptoEngine) this.Aes;
            return padAlgo1.encrypt(keyAlgorithm, keyValue, textToBeEncrypted, params);
        } else {
            if (keyAlgorithm == 9 && keyValue != null && keyValue.length == 16) {
                if (params == null) {
                    this.Aes.aesEncrypt(keyValue, textToBeEncrypted);
                } else {
                    PaddingAlgo padAlgo = params.getPaddingAlgo();
                    ProcessingAlgo procAlgo = params.getProcessingAlgo();
                    if ((padAlgo == null || padAlgo == PaddingAlgo.NONE) && (procAlgo == null || procAlgo == ProcessingAlgo.ECB) && params.getIV() == null) {
                        this.Aes.aesEncrypt(keyValue, textToBeEncrypted);
                    }
                }
            }

            throw new CipurseException("Crypto Engine doesn\'t support this functionality");
        }
    }

    public byte[] decryptText(byte keyAlgorithm, byte[] keyValue, byte[] textToBeDecrypted, CryptoParameters params) throws CipurseException {
        if (this.Aes instanceof ICryptoEngine) {
            ICryptoEngine padAlgo1 = (ICryptoEngine) this.Aes;
            return padAlgo1.decrypt(keyAlgorithm, keyValue, textToBeDecrypted, params);
        } else {
            if (keyAlgorithm == 9 && keyValue != null && keyValue.length == 16) {
                if (params == null) {
                    this.Aes.aesDecrypt(keyValue, textToBeDecrypted);
                } else {
                    PaddingAlgo padAlgo = params.getPaddingAlgo();
                    ProcessingAlgo procAlgo = params.getProcessingAlgo();
                    if ((padAlgo == null || padAlgo == PaddingAlgo.NONE) && (procAlgo == null || procAlgo == ProcessingAlgo.ECB) && params.getIV() == null) {
                        this.Aes.aesDecrypt(keyValue, textToBeDecrypted);
                    }
                }
            }

            throw new CipurseException("Crypto Engine doesn\'t support this functionality");
        }
    }

    /**
     * Convenience method to build a GET CHALLENGE command in the APDU buffer
     *
     **/
    public byte[] buildGetChallenge(CommandApdu commandApdu) {
        RP = this.cipurseCrypto.getRandom(16);
        rP = this.cipurseCrypto.getRandom(6);
        if (commandApdu.getLc() > CHALLENGES_MAX_LENGTH)
            throw new Iso7816Exception(SwEnum.SW_WRONG_DATA);
        byte[] response = new byte[22];
        System.arraycopy(RP, 0, response, 0, 16);
        System.arraycopy(rP, 0, response, 16, 6);
        return response;
    }

    /**
     * Method to build MUTUAL AUTENTICATE command based on the previously derived
     * session key k0 and the random data generated with finishGetChallenge().
     *
     * @param buffer
     * @param length
     */
    public void finishGetChallenge(byte[] buffer, short length) {
        if (length > CHALLENGES_MAX_LENGTH)
            throw new Iso7816Exception(SwEnum.SW_WRONG_DATA);

        System.arraycopy(RP, 0, buffer, 0, 16);
        System.arraycopy(rP, 0, buffer, 16, 6);

    }

    public void setSelectedKey(byte[] selectedKey) {
        this.selectedKey = selectedKey;
    }

    public short buildMutualAuthenticate(byte[] buffer, short offset, short keyID) {
        byte[] mutualAuth = new byte[38];
        System.arraycopy(Ct, 0, mutualAuth, 0, 16);
        System.arraycopy(RP, 0, mutualAuth, 16, 16);
        System.arraycopy(rP, 0, mutualAuth, 32, 6);

        mutualAuthCmd = new byte[this.mutualAuthHeader.length + mutualAuth.length + 2];
        System.arraycopy(this.mutualAuthHeader, 0, mutualAuthCmd, 0, this.mutualAuthHeader.length);
        System.arraycopy(mutualAuth, 0, mutualAuthCmd, 5, mutualAuth.length);
        mutualAuthCmd[4] = (byte) (mutualAuth.length & 255);
        mutualAuthCmd[mutualAuthCmd.length - 1] = 16;
        selectedKey = keySet[keyID - 1].clone();
        return 0;
    }

    public byte[] finishMutualAuthenticate(byte[] buffer) {
        try {
            byte[] response = new byte[16];
            byte[] cP = new byte[16];
            byte[] RT = new byte[16];
            byte[] rT = new byte[6];
            System.arraycopy(buffer, 5, cP, 0, 16);
            System.arraycopy(buffer, 21, RT, 0, 16);
            System.arraycopy(buffer, 37, rT, 0, 6);

            byte[] cP1 = this.cipurseCrypto.generateK0AndGetCp(selectedKey, RP, rP, RT, rT);

            if (Arrays.equals(cP1, cP)) {
                byte[] Ct = this.cipurseCrypto.generateCT(RT);
                System.arraycopy(Ct, 0, response, 0, Ct.length);
            } else {
                this.logger.log("Terminal response verification failed");
                throw new Iso7816Exception(SwEnum.SW_WRONG_DATA);
            }
            return response;
        } catch (CipurseException ex) {
            logger.log(1, ex.getMessage());
            throw new Iso7816Exception(SwEnum.SW_WRONG_DATA);
        }
    }

    /**
     * Unwrap card response and store output data in the specified buffer.
     *
     * @param inBuffer
     * @param inOffset
     * @param inLength
     * @param outBuffer
     * @param outOffset
     * @return
     */
    public short unwrap(byte[] inBuffer, short inOffset, short inLength, byte[] outBuffer, short outOffset) {
        return 0;
    }

    /**
     * Wraps a command with the specified security level.
     *
     * @param smi
     * @param inBuffer
     * @param inOffset
     * @param inLength
     * @param outBuffer
     * @param outOffset
     * @return
     */
    public short wrap(short smi, byte[] inBuffer, short inOffset, short inLength, byte[] outBuffer, short outOffset) {
        return 0;
    }

    /**
     * Resets the authentication state and clears all session keys.
     */
    public void resetSecurity() {

    }
}