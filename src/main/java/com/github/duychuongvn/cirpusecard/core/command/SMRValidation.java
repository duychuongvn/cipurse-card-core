package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.constant.SwEnum;
import com.github.duychuongvn.cirpusecard.core.exception.Iso7816Exception;
import org.osptalliance.cipurse.commands.SMR;

/**
 * Created by huynhduychuong on 5/13/2016.
 */
public class SMRValidation {

    public boolean isValidSMI(CommandApdu commandApdu, SMR smr) {

        byte smgroup = 0;
        if (commandApdu.getCommandEnum() == CommandEnum.CREATE_FILE
                || commandApdu.getCommandEnum() == CommandEnum.UPDATE_FILE_ATTRIBUTES) {
            smgroup = smr.SMGroup_4;
        } else if (commandApdu.getCommandEnum() == CommandEnum.READ_FILE_ATTRIBUTES) {
            smgroup = smr.SMGroup_3;
        } else if (commandApdu.getCommandEnum() == CommandEnum.UPDATE_KEY
                || commandApdu.getCommandEnum() == CommandEnum.UPDATE_KEY_ATTRIBUTES) {
            smgroup = smr.SMGroup_2;
        } else if (commandApdu.getCommandEnum() == CommandEnum.ACTIVATE_FILE
                || commandApdu.getCommandEnum() == CommandEnum.DELETE_FILE
                || commandApdu.getCommandEnum() == CommandEnum.CREATE_FILE.DEACTIVATE_FILE) {
            smgroup = smr.SMGroup_1;
        }

        return isValidSMI(commandApdu, smgroup);

    }

    public boolean isValidSMI(CommandApdu commandApdu, byte smgroup) {
        byte commandSmi = commandApdu.getSmi();
        int requestSMI = (commandSmi & 0xF0) >> 4;
        int responseSMI = commandSmi & 0x0F;
        if (smgroup == SMR.SM_PLAIN_PLAIN
                || (smgroup == SMR.SM_PLAIN_MAC && (isMac(responseSMI)))
                || (smgroup == SMR.SM_PLAIN_ENC && (isENC(responseSMI)))
                || (smgroup == SMR.SM_MAC_PLAIN && (isMac(requestSMI)))
                || (smgroup == SMR.SM_ENC_PLAIN && (isENC(requestSMI)))
                || (smgroup == SMR.SM_MAC_MAC && (isMac(requestSMI) && isMac(responseSMI)))
                || (smgroup == SMR.SM_MAC_ENC && (isMac(requestSMI) && isENC(responseSMI)))
                || (smgroup == SMR.SM_ENC_MAC && (isENC(requestSMI) && isMac(responseSMI)))
                || (smgroup == SMR.SM_ENC_ENC && (isENC(requestSMI) && isENC(responseSMI)))) {
            return true;
        }
        throw new Iso7816Exception(SwEnum.SW_SECURITY_STATUS_NOT_SATISFIED);
    }

    boolean isENC(int octetSMI) {
        return octetSMI == 8 || octetSMI == 9;
    }

    private boolean isMac(int octetSMI) {
        return octetSMI == 4 || octetSMI == 5;
    }
}
