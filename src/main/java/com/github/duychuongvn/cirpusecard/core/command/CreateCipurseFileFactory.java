package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.command.impl.ADFFileImpl;
import com.github.duychuongvn.cirpusecard.core.constant.SwEnum;
import com.github.duychuongvn.cirpusecard.core.exception.Iso7816Exception;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;

/**
 * Created by huynhduychuong on 5/5/2016.
 */
public class CreateCipurseFileFactory {

    private static final short DGI_CREATE_ADF = -28160; // byte[]{(byte) 0x92, (byte) 0x00};
    private static final short DGI_CREATE_EF = -28159; // byte[]{(byte) 0x92, (byte) 0x01};

    public static CipurseFile createInstance(CommandApdu commandApdu, ADFFile adfFile) {
        byte[] data = commandApdu.getData();
        byte[] dgiBytes = new byte[2];
        System.arraycopy(data, 0, dgiBytes, 0, dgiBytes.length);
        short dgi = ByteUtils.getShort(dgiBytes, (short) 0);
        switch (dgi) {
            case DGI_CREATE_ADF:
                ADFFile childADFFile = new ADFFileImpl();
                childADFFile.createFile(commandApdu);
                return childADFFile;
            case DGI_CREATE_EF:
                adfFile.createEF(commandApdu);
                return adfFile.getCurrentEF();
            default:
                throw new Iso7816Exception(SwEnum.SW_COMMAND_NOT_ALLOWED);
        }
    }
}
