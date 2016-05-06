package com.github.duychuongvn.cirpusecard.core.command.impl;

import com.github.duychuongvn.cirpusecard.core.command.ADFFile;
import com.github.duychuongvn.cirpusecard.core.command.BinaryFile;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import com.github.duychuongvn.cirpusecard.core.util.CommandApdu;
import org.osptalliance.cipurse.commands.*;

/**
 * Created by huynhduychuong on 5/5/2016.
 */
public class BinaryFileImpl extends CipurseFileImpl implements BinaryFile {
    private SMR smr;
    private ART[] art;
    private EFFileAttributes efFileAttributes;
    private byte[] content;

    public BinaryFileImpl(ADFFile currentADF) {
        super(currentADF);
    }

    public byte[] readBinary(CommandApdu commandApdu) {
        // 00 B0 00 00 le
        int responseLength;
        int offset = getOffset(commandApdu.getP1(), commandApdu.getP2());
        if (commandApdu.getLe() < content.length) {
            responseLength = commandApdu.getLe();
        } else {
            responseLength = content.length;
        }
        // TODO: Check response Length and offset with content length
        byte[] response = new byte[responseLength];
        System.arraycopy(content, offset, response, 0, responseLength);
        return response;
    }

    private int getOffset(int p1, int p2) {
        int offset;
        if(ByteUtils.matchBitByBitIndex((byte) p1, 7)) {
            offset = p2;
        } else {
            byte[] offsetBytes = new byte[2];
            offsetBytes[0] = (byte)(p1 & 0x7F);
            offsetBytes[1] = (byte) p2;
            offset = ByteUtils.byteArrayToInt(offsetBytes);
        }
        return offset;
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
        byte[] data = commandApdu.getData();
        int fileTypeIndex = 3;
        int sfidIndex = fileTypeIndex + 1;
        int fileIDIndex = sfidIndex + 1;
        int fileAttributeIndex = fileIDIndex + 2;
        int numOfKeyIndex = fileAttributeIndex + 2;
        int smrIndex = numOfKeyIndex + 1;
        int artIndex = smrIndex + 2;
        efFileAttributes = new EFFileAttributes();
        efFileAttributes.fileType = data[fileTypeIndex];
        efFileAttributes.SFID = data[sfidIndex];
        efFileAttributes.fileID = ByteUtils.getShort(data, (short) fileIDIndex);
        byte[] fileAttributes = new byte[2];
        System.arraycopy(data, fileAttributeIndex, fileAttributes, 0, fileAttributes.length);
        efFileAttributes.numOfRecs = 0;
        efFileAttributes.fileSize = fileAttributes[1];
        efFileAttributes.numOfKeys = data[numOfKeyIndex];


        byte[] smrData = new byte[2];
        System.arraycopy(data, smrIndex, smrData, 0, smrData.length);
        byte[] artBytes = new byte[efFileAttributes.numOfKeys + 1];

        art = new ART[efFileAttributes.numOfKeys + 1];
        System.arraycopy(data, artIndex, artBytes, 0, artBytes.length);

        for (int i = 0; i < art.length; i++) {
            int acgValue = artBytes[i];
            art[i] = new ART(acgValue);
        }
        smr = new SMR(new ByteArray(smrData));
        content = new byte[efFileAttributes.fileSize];
        return new byte[0];
    }

    public byte[] readFileAttributes(CommandApdu commandApdu) {
        return new byte[0];
    }

    public byte[] updateFileAttributes(CommandApdu commandApdu) {
        return new byte[0];
    }

    public EFFileAttributes getEfFileAttributes() {
        return efFileAttributes;
    }

    public byte[] getContent() {
        return content;
    }

    public SMR getSmr() {
        return smr;
    }

    public ART[] getArt() {
        return art;
    }
}
