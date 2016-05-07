package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.command.impl.ADFFileImpl;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;

/**
 * Created by huynhduychuong on 5/5/2016.
 */
public class CreateCipurseFileFactory {

    public static void main(String[] args) {
        System.out.println(ByteUtils.getShort(new byte[]{(byte) 0x92, (byte) 0x01}, (short)0));
    }
    private static final short DGI_CREATE_ADF = -28160; // byte[]{(byte) 0x92, (byte) 0x00};
    private static final short DGI_CREATE_EF =  -28159; // byte[]{(byte) 0x92, (byte) 0x01};
    public static CipurseFile createInstance(CommandApdu commandApdu, ADFFile adfFile) {
        byte[] data = commandApdu.getData();
        byte[] dgiBytes = new byte[2];
        System.arraycopy(data, 0, dgiBytes, 0, dgiBytes.length);
        short dgi = ByteUtils.getShort(dgiBytes, (short)0);
        String dgiHex = ByteUtils.bytesToHexString(dgiBytes);
        switch (dgi) {
            case DGI_CREATE_ADF:
                ADFFile childADFFile = new  ADFFileImpl();
                childADFFile.createFile(commandApdu);
                return childADFFile;
            case DGI_CREATE_EF:
                 adfFile.createEF(commandApdu);
                 return adfFile.getCurrentEF();
        }
        return null;
    }
}
