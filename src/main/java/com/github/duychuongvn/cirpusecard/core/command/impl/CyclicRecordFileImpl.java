package com.github.duychuongvn.cirpusecard.core.command.impl;

import com.github.duychuongvn.cirpusecard.core.command.ADFFile;
import com.github.duychuongvn.cirpusecard.core.command.CommandApdu;
import com.github.duychuongvn.cirpusecard.core.command.CyclicRecordFile;
import com.github.duychuongvn.cirpusecard.core.command.ElementFile;
import com.github.duychuongvn.cirpusecard.core.constant.SwEnum;
import com.github.duychuongvn.cirpusecard.core.exception.Iso7816Exception;
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
        byte[] data = commandApdu.getData();
        int recordNumber = getRecordNumber(commandApdu);
        System.arraycopy(data, 0, cyclicFiles[recordNumber - 1], 0, commandApdu.getLc());
        return new byte[0];
    }

    private int getRecordNumber(CommandApdu commandApdu) {
        int recordNumber = commandApdu.getP1();
        if (recordNumber < 1 || recordNumber > efFileAttributes.numOfRecs) {
            throw new Iso7816Exception(SwEnum.SW_WRONG_P1P2);
        }
        return recordNumber;
    }

    public byte[] readRecord(CommandApdu commandApdu) {
        int recordNumber = getRecordNumber(commandApdu);
        return cyclicFiles[recordNumber - 1].clone();
    }
}
