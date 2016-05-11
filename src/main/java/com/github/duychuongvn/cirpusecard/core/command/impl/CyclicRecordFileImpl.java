package com.github.duychuongvn.cirpusecard.core.command.impl;

import com.github.duychuongvn.cirpusecard.core.command.ADFFile;
import com.github.duychuongvn.cirpusecard.core.command.CommandApdu;
import com.github.duychuongvn.cirpusecard.core.command.CyclicRecordFile;
import com.github.duychuongvn.cirpusecard.core.command.ElementFile;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;

/**
 * Created by huynhduychuong on 5/11/2016.
 */
public class CyclicRecordFileImpl extends ElementFileImpl implements CyclicRecordFile {
    private byte[][] cyclicFiles;

    public CyclicRecordFileImpl(ADFFile currentADF) {
        super(currentADF);
    }

    @Override
    public byte[] createFile(CommandApdu commandApdu) {
        super.createFile(commandApdu);
        // 92 01 0D 06 00 30 01 05 64 03 5A 5A FF FF FF FF
        byte[] commandData = commandApdu.getData();
        int numberOfRecIndex = 7;
        efFileAttributes.RecSize = (short) efFileAttributes.fileSize;
        efFileAttributes.numOfRecs = ByteUtils.byteToShort(commandData[numberOfRecIndex]);
        cyclicFiles = new byte[efFileAttributes.numOfRecs][efFileAttributes.RecSize];
        return new byte[0];
    }

    public byte[] execute(CommandApdu commandApdu) {
        return new byte[0];
    }

    public byte[] appendRecord(CommandApdu commandApdu) {
        return new byte[0];
    }

    public byte[] updateRecord(CommandApdu commandApdu) {
        return new byte[0];
    }

    public byte[] readRecord(CommandApdu commandApdu) {
        return new byte[0];
    }
}
