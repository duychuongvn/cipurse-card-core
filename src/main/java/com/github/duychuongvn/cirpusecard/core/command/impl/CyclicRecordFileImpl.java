package com.github.duychuongvn.cirpusecard.core.command.impl;

import com.github.duychuongvn.cirpusecard.core.command.ADFFile;
import com.github.duychuongvn.cirpusecard.core.command.CommandApdu;
import com.github.duychuongvn.cirpusecard.core.command.CyclicRecordFile;
import com.github.duychuongvn.cirpusecard.core.command.ElementFile;
import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
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
        if (commandApdu.getCommandEnum() == CommandEnum.UPDATE_RECORD) {
            return updateRecord(commandApdu);
        } else if (commandApdu.getCommandEnum() == CommandEnum.READ_RECORD) {
            return readRecord(commandApdu);
        } else if (commandApdu.getCommandEnum() == CommandEnum.APPEND_RECORD) {
            return appendRecord(commandApdu);
        } else {
            throw new IllegalArgumentException("Not implemented");
        }
    }

    public byte[] appendRecord(CommandApdu commandApdu) {
        if (commandApdu.getP1() != 0) {
            throw new Iso7816Exception(SwEnum.SW_WRONG_P1P2);
        }
        for (int i = 0; i < cyclicFiles.length - 1; i++) {
            cyclicFiles[i + 1] = cyclicFiles[i].clone();
        }

        byte[] newRecord = new byte[efFileAttributes.RecSize];

        System.arraycopy(commandApdu.getData(), 0, newRecord, 0, commandApdu.getLc());
        cyclicFiles[0] = newRecord;
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
        //TODO: check P2 to return records from record number to last
        return cyclicFiles[recordNumber - 1].clone();
    }
}
