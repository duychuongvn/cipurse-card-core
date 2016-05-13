package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.constant.SwEnum;
import com.github.duychuongvn.cirpusecard.core.exception.Iso7816Exception;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import junit.framework.TestCase;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.osptalliance.cipurse.commands.CipurseCardHandler;
import org.osptalliance.cipurse.commands.DFFileAttributes;
import org.osptalliance.cipurse.commands.SMR;

/**
 * Created by huynhduychuong on 5/13/2016.
 */
public class SMRValidationTest {

    private SMR smrADf;
    private SMR smrEF;

    @Before
    public void init() {
        smrADf = new SMR();
        smrADf.SMGroup_1 = SMR.SM_PLAIN_PLAIN; // ACTIVATE_FILE (ADF), DEACTIVATE_FILE (ADF), DELETE_FILE (EF or ADF)
        smrADf.SMGroup_2 = SMR.SM_ENC_ENC; // UPDATE_KEY, UPDATE_KEY_ATTRIBUTES
        smrADf.SMGroup_3 = SMR.SM_MAC_ENC; // READ_FILE_ATTRIBUTES
        smrADf.SMGroup_4 = SMR.SM_ENC_ENC; // UPDATE FILE ATTRIBUTES, CREATE_FILE (EF)
        smrEF = new SMR();
        smrEF.SMGroup_1 = SMR.SM_ENC_ENC; // READ BINARY or READ RECORD
        smrEF.SMGroup_2 = SMR.SM_ENC_PLAIN; // UPDATE BINARY or UPDATE / APPEND RECORD
        smrEF.SMGroup_3 = SMR.SM_MAC_ENC; // READ FILE ATTRIBUTE
        smrEF.SMGroup_4 = SMR.SM_ENC_MAC; // UPDATE FILE ATTRIBUTE
    }

    @Test
    public void shouldReceive6982WhenIndicateWrongSMI() throws Exception {

        String command = "92 01 0C 11 00 30 00 00 64 02 06 00 7F FF FF";
        SMRValidation smrValidation = new SMRValidation();
        try {
            smrValidation.isValidSMI(new CommandApdu(CommandEnum.CREATE_FILE, 00, 00, ByteUtils.fromHexString(command)), smrADf);
        } catch (Iso7816Exception exception) {
            Assertions.assertThat(exception.getResponseStatus()).isEqualTo(SwEnum.SW_SECURITY_STATUS_NOT_SATISFIED.toBytes());
        }

    }

    @Test
    public void shouldReturnTrueIfCreateFileAndValidSMR() {

        SMRValidation smrValidation = new SMRValidation();
        boolean result;
        String command;
        smrADf.SMGroup_4 = SMR.SM_ENC_ENC;
        command = "04 E0 00 00 05 89 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 88 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();

        command = "04 E0 00 00 05 44 92 01 0C 11 01";
        smrADf.SMGroup_4 = SMR.SM_MAC_MAC;
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 45 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();

        command = "04 E0 00 00 05 48 92 01 0C 11 01";
        smrADf.SMGroup_4 = SMR.SM_MAC_PLAIN;
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 49 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 40 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();


        command = "04 E0 00 00 05 48 92 01 0C 11 01";
        smrADf.SMGroup_4 = SMR.SM_MAC_PLAIN;
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 49 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 40 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();


        smrADf.SMGroup_4 = SMR.SM_ENC_PLAIN;
        command = "04 E0 00 00 05 88 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 84 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 80 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();

        smrADf.SMGroup_4 = SMR.SM_PLAIN_PLAIN;
        command = "04 E0 00 00 05 88 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 84 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 80 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();

        smrADf.SMGroup_4 = SMR.SM_PLAIN_MAC;
        command = "04 E0 00 00 05 84 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 85 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 44 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 45 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 04 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 05 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();


        smrADf.SMGroup_4 = SMR.SM_MAC_ENC;
        command = "04 E0 00 00 05 48 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 49 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();


        smrADf.SMGroup_4 = SMR.SM_ENC_MAC;
        command = "04 E0 00 00 05 84 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 85 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();


        smrADf.SMGroup_4 = SMR.SM_PLAIN_ENC;
        command = "04 E0 00 00 05 88 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 89 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        smrADf.SMGroup_4 = SMR.SM_PLAIN_ENC;
        command = "04 E0 00 00 05 48 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 49 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();

        smrADf.SMGroup_4 = SMR.SM_PLAIN_ENC;
        command = "04 E0 00 00 05 08 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
        command = "04 E0 00 00 05 09 92 01 0C 11 01";
        result = smrValidation.isValidSMI(CommandApdu.createInstanceFromSMCommand(ByteUtils.fromHexString(command)), smrADf);
        Assertions.assertThat(result).isTrue();
    }
}