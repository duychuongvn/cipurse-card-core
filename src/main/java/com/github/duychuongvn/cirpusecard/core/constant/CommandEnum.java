package com.github.duychuongvn.cirpusecard.core.constant;

import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;

/**
 * Created by huynhduychuong on 4/26/2016.
 */
public enum CommandEnum implements EnumKey {
    SELECT(0x00, 0xA4),
    READ_BINARY(0x00, 0xB0),
    UPDATE_BINARY(0x00, 0xD6),
    READ_RECORD(0x00, 0xB2),
    UPDATE_RECORD(0x00, 0xDC),
    APPEND_RECORD(0x00, 0xE2),
    READ_VALUE(0x80, 0x34),
    INCREASE_VALUE(0x80, 0x32),
    DECREASE_VALUE(0x80, 0x30),
    PERFORM_TRANSACTION(0x80, 0x7E),
    CANCEL_TRANSACTION(0x80, 0x7C),
    CREATE_FILE(0x00, 0xE0),
    DELETE_FILE(0x00, 0xE4),
    ACTIVATE_FILE(0x00, 0x44),
    DEACTIVATE_FILE(0x00, 0x04),
    GET_CHALLENGE(0x00, 0x84),
    MUTUAL_AUTHENTICATE(0x00, 0x82),
    UPDATE_FILE_ATTRIBUTES(0x00, 0xDE),
    UPDATE_KEY(0x00, 0x52),
    UPDATE_KEY_ATTRIBUTES(0x00, 0x4E),
    READ_FILE_ATTRIBUTES(0x80, 0xCE),;
    private int cla;
    private int ins;

    CommandEnum(int cla, int ins) {
        this.cla = cla;
        this.ins = ins;
    }

    public int getKey() {
        return ByteUtils.byteArrayToInt(toBytes());
    }

    public byte[] toBytes() {
        return new byte[]{(byte) this.cla, (byte) this.ins};
    }

    public int getCla() {
        return cla;
    }

    public int getIns() {
        return ins;
    }
}
