package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.command.ADFFile;
import com.github.duychuongvn.cirpusecard.core.command.CipurseFile;
import com.github.duychuongvn.cirpusecard.core.command.impl.BinaryFileImpl;
import com.github.duychuongvn.cirpusecard.core.util.CommandApdu;
import org.osptalliance.cipurse.commands.DFFileAttributes;
import org.osptalliance.cipurse.commands.EFFileAttributes;

/**
 * Created by huynhduychuong on 5/5/2016.
 */
public class CipurseFileFactory {
    public static CipurseFile createInstance(CommandApdu commandApdu, ADFFile adfFile) {
        byte[] data = commandApdu.getData();
        int fileType = data[3];
        switch (fileType) {
            case EFFileAttributes.BINARY_FILE_TRANS_TYPE:
            case EFFileAttributes.BINARY_FILE_TYPE:
                return new BinaryFileImpl(adfFile);
            case EFFileAttributes.CYCLIC_RECORD_TRANS_TYPE:
            case EFFileAttributes.CYCLIC_RECORD_TYPE:
                break;
            case EFFileAttributes.LINEAR_RECORD_TRANS_TYPE:
            case EFFileAttributes.LINEAR_RECORD_TYPE :
                break;
            case EFFileAttributes.VALUE_FILE_TRANS_TYPE:
            case EFFileAttributes.VALUE_FILE_TYPE:
                break;
        }
        return null;
    }
}
