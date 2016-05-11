package com.github.duychuongvn.cirpusecard.core.exception;

import com.github.duychuongvn.cirpusecard.core.constant.SwEnum;

/**
 * Created by huynhduychuong on 5/10/2016.
 */
public class Iso7816Exception extends RuntimeException {
    private SwEnum swEnum;
    public Iso7816Exception(SwEnum swEnum) {
        super(swEnum.getDescription());
        this.swEnum = swEnum;
    }

    public byte[] getResponseStatus() {
        return swEnum.toBytes();
    }

}
