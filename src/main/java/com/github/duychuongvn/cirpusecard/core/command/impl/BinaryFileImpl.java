package com.github.duychuongvn.cirpusecard.core.command.impl;

import com.github.duychuongvn.cirpusecard.core.command.ADFFile;
import com.github.duychuongvn.cirpusecard.core.command.BinaryFile;
import com.github.duychuongvn.cirpusecard.core.command.CommandApdu;
import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;

import org.osptalliance.cipurse.commands.ART;
import org.osptalliance.cipurse.commands.ByteArray;
import org.osptalliance.cipurse.commands.EFFileAttributes;
import org.osptalliance.cipurse.commands.SMR;

/**
 * Created by huynhduychuong on 5/5/2016.
 */
public class BinaryFileImpl extends ElementFileImpl implements BinaryFile {


    private byte[] content;

    public BinaryFileImpl(ADFFile currentADF) {
        super(currentADF);
    }

    public byte[] readBinary(CommandApdu commandApdu) {
        // 00 B0 00 00 le
        int offset = getOffset(commandApdu.getP1(), commandApdu.getP2());
        int responseLength = getResponseLength(commandApdu);
        // TODO: Check response Length and offset with content length
        byte[] response = new byte[responseLength];
        System.arraycopy(content, offset, response, 0, responseLength);
        return response;
    }



    public byte[] updateBinary(CommandApdu commandApdu) {
        // 00 D6 00 00 lc data
        int offset = getOffset(commandApdu.getP1(), commandApdu.getP2());
        int lc = commandApdu.getLc();
        System.arraycopy(commandApdu.getData(), 0, content, offset, lc);
        return new byte[0];
    }

    public byte[] createFile(CommandApdu commandApdu) {
        // "92 01 0C 11 00 30 00 00 64 02 06 00 7F FF FF";

        super.createFile(commandApdu);
        content = new byte[efFileAttributes.fileSize];
        return new byte[0];
    }




    public byte[] execute(CommandApdu commandApdu) {
        if(commandApdu.getCommandEnum() == CommandEnum.UPDATE_BINARY) {
            return updateBinary(commandApdu);
        } else if(commandApdu.getCommandEnum() == CommandEnum.READ_BINARY) {
            return readBinary(commandApdu);
        } else {
            throw new IllegalArgumentException("Not implemented");
        }
    }

    public byte[] getContent() {
        return content;
    }


}
