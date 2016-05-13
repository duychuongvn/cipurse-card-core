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
    private int selectedKeyNo;
    private short _0 = 0;
    private byte[] rP;
    private byte[] RP;
    private byte[] mutualAuthCmd = null;
    private byte[] Ct;
    private byte SMI;
    private boolean isSecuredMessage;

    public static CipurseSecureMessage getInstance(IAes aes, ILogger logger) {
        if (cipurseSecureMessage == null) {
            cipurseSecureMessage = new CipurseSecureMessage(aes, logger);
        }
        return cipurseSecureMessage;
    }


    private CipurseSecureMessage(IAes Aes, ILogger logger) {
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

    /**
     * Convenience method to build a GET CHALLENGE command in the APDU buffer
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

    public byte[] finishMutualAuthenticate(byte[] commandApdu) {
        try {
            selectedKeyNo = ByteUtils.byteToInt(commandApdu[3]);
            selectedKey = keySet[selectedKeyNo - 1];
            byte[] response = new byte[16];
            byte[] cP = new byte[16];
            byte[] RT = new byte[16];
            byte[] rT = new byte[6];
            System.arraycopy(commandApdu, 5, cP, 0, 16);
            System.arraycopy(commandApdu, 21, RT, 0, 16);
            System.arraycopy(commandApdu, 37, rT, 0, 6);

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
            resetSecurity();
            throw new Iso7816Exception(SwEnum.SW_WRONG_DATA);
        }
    }

    /**
     * Resets the authentication state and clears all session keys.
     */
    public void resetSecurity() {
        this.SMI = 0;
        this.selectedKey = nullVector;
    }

    public int getSelectedKeyNo() {
        return selectedKeyNo;
    }
}