package com.github.duychuongvn.cirpusecard.core.iso7816;

/**
 * Created by huynhduychuong on 5/11/2016.
 */
public interface ISO7816 {
    short SW_NO_ERROR = -28672;
    short SW_BYTES_REMAINING_00 = 24832;
    short SW_WRONG_LENGTH = 26368;
    short SW_SECURITY_STATUS_NOT_SATISFIED = 27010;
    short SW_FILE_INVALID = 27011;
    short SW_DATA_INVALID = 27012;
    short SW_CONDITIONS_NOT_SATISFIED = 27013;
    short SW_COMMAND_NOT_ALLOWED = 27014;
    short SW_APPLET_SELECT_FAILED = 27033;
    short SW_WRONG_DATA = 27264;
    short SW_FUNC_NOT_SUPPORTED = 27265;
    short SW_FILE_NOT_FOUND = 27266;
    short SW_RECORD_NOT_FOUND = 27267;
    short SW_INCORRECT_P1P2 = 27270;
    short SW_WRONG_P1P2 = 27392;
    short SW_CORRECT_LENGTH_00 = 27648;
    short SW_INS_NOT_SUPPORTED = 27904;
    short SW_CLA_NOT_SUPPORTED = 28160;
    short SW_COMMAND_CHAINING_NOT_SUPPORTED = 26756;
    short SW_LAST_COMMAND_EXPECTED = 26755;
    short SW_UNKNOWN = 28416;
    short SW_FILE_FULL = 27268;
    short SW_LOGICAL_CHANNEL_NOT_SUPPORTED = 26753;
    short SW_SECURE_MESSAGING_NOT_SUPPORTED = 26754;
    short SW_WARNING_STATE_UNCHANGED = 25088;
    byte OFFSET_CLA = 0;
    byte OFFSET_INS = 1;
    byte OFFSET_P1 = 2;
    byte OFFSET_P2 = 3;
    byte OFFSET_LC = 4;
    byte OFFSET_CDATA = 5;
    byte CLA_ISO7816 = 0;
    byte INS_SELECT = -92;
    byte INS_EXTERNAL_AUTHENTICATE = -126;
}
