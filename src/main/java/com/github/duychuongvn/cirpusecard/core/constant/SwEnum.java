package com.github.duychuongvn.cirpusecard.core.constant;

import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;

/**
 * Created by huynhduychuong on 4/26/2016.
 */
public enum SwEnum implements EnumKey {

    SW_NO_ERROR("90 00", "No Error = (short)0x9000"),
    SW_APPLET_SELECT_FAILED("69 99", "Applet selection failed = 0x6999"),
    SW_BYTES_REMAINING_00("61 00", "Response bytes remaining = 0x6100"),
    SW_CLA_NOT_SUPPORTED("6E 00", "CLA value not supported = 0x6E00"),
    SW_COMMAND_CHAINING_NOT_SUPPORTED("68 84", "Command chaining not supported = 0x6884"),
    SW_COMMAND_NOT_ALLOWED("69 86", "Command not allowed (no current EF) = 0x6986"),
    SW_CONDITIONS_NOT_SATISFIED("69 85", "Conditions of use not satisfied = 0x6985"),
    SW_CORRECT_LENGTH_00("6C 00", "Correct Expected Length (Le) = 0x6C00"),
    SW_DATA_INVALID("69 84", "Data invalid = 0x6984"),
    SW_FILE_FULL("6A 84", "Not enough memory space in the file = 0x6A84"),
    SW_FILE_INVALID("69 83", "File invalid = 0x6983"),
    SW_FILE_NOT_FOUND("6A 82", "File not found = 0x6A82"),
    SW_FUNC_NOT_SUPPORTED("6A 81", "Function not supported = 0x6A81"),
    SW_INCORRECT_P1P2("6A 86", "Incorrect parameters (P1,P2) = 0x6A86"),
    SW_INS_NOT_SUPPORTED("6D 00", "INS value not supported = 0x6D00"),
    SW_LAST_COMMAND_EXPECTED("68 83", "Last command in chain expected = 0x6883"),
    SW_LOGICAL_CHANNEL_NOT_SUPPORTED("68 81", "Card does not support the operation on the specified logical channel = 0x6881"),
    SW_RECORD_NOT_FOUND("6A 83", "Record not found = 0x6A83"),
    SW_SECURE_MESSAGING_NOT_SUPPORTED("68 82", "Card does not support secure messaging = 0x6882"),
    SW_SECURITY_STATUS_NOT_SATISFIED("69 82", "Security condition not satisfied = 0x6982"),
    SW_UNKNOWN("6F 00", "No precise diagnosis = 0x6F00"),
    SW_WARNING_STATE_UNCHANGED("62 00", "Warning, card state unchanged = 0x6200"),
    SW_WRONG_DATA("6A 80", "Wrong data = 0x6A80"),
    W_WRONG_LENGTH("67 00", "Wrong length = 0x6700"),
    SW_WRONG_P1P2("6B 00", "Incorrect parameters (P1,P2) = 0x6B00"),;
    private byte[] code;
    private String description;

    SwEnum(String code, String description) {

        this.code = ByteUtils.fromHexString(code);
        this.description = description;
    }

    public int getKey() {
        return ByteUtils.byteArrayToInt(this.code);
    }
    public byte[] toBytes() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public static SwEnum getSW(byte[] pData) {
        SwEnum ret = null;
        if (pData != null && pData.length >= 2) {
            for (SwEnum val : values()) {
                if (val.code.length == 1 && pData[pData.length - 2] == val.code[0]
                        || pData[pData.length - 2] == val.code[0] && pData[pData.length - 1] == val.code[1]) {
                    ret = val;
                    break;
                }
            }
        }
        return ret;
    }
}
