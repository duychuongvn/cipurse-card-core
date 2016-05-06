package com.github.duychuongvn.cirpusecard.core.exception;

import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;

/**
 * Created by huynhduychuong on 4/27/2016.
 */
public class EnumParserException extends RuntimeException {

    public EnumParserException(int id, String className) {
        super("Cannot parse enum for class: " + className + " - Bytes Id: " + ByteUtils.bytesToHexString(ByteUtils.toByteArray(id)));
    }
}
