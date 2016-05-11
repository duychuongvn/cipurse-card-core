package com.github.duychuongvn.cirpusecard.core.command.impl;

import com.github.duychuongvn.cirpusecard.core.command.ADFFile;
import com.github.duychuongvn.cirpusecard.core.command.CommandApdu;
import com.github.duychuongvn.cirpusecard.core.command.ElementFile;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.osptalliance.cipurse.commands.ART;
import org.osptalliance.cipurse.commands.ByteArray;
import org.osptalliance.cipurse.commands.EFFileAttributes;
import org.osptalliance.cipurse.commands.SMR;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public abstract class ElementFileImpl implements ElementFile {
    private static final int FILE_ATTRIBUTES_RESPONSE_LENGTH_WITHOUT_ART = 9;
    protected boolean activated;
    protected ADFFile currentADF;
    protected EFFileAttributes efFileAttributes;
    protected SMR smr;
    protected ART[] arts;

    public ElementFileImpl(ADFFile currentADF) {
        this.currentADF = currentADF;
    }

    public byte[] readFileAttributes(CommandApdu commandApdu) {
        int responseLength = FILE_ATTRIBUTES_RESPONSE_LENGTH_WITHOUT_ART + efFileAttributes.numOfKeys + 1;
        byte[] responseBytes = new byte[responseLength];
        int fdIndex = 0;
        int sfgIndex = fdIndex + 1;
        int fidIndex = sfgIndex + 1;
        int fileDataAttributeIndex = fidIndex + 2;
        int nbrKIndex = fileDataAttributeIndex + 2;
        int smrIndex = nbrKIndex + 1;
        int artIndex = smrIndex + 2;
        responseBytes[fdIndex] = (byte) efFileAttributes.fileType;
        byte[] fidBytes = ByteUtils.intToBytes(efFileAttributes.fileID, 2);
        System.arraycopy(fidBytes, 0, responseBytes, fidIndex, fidBytes.length);
        byte[] fileAttributeBytes = new byte[2];
        if (efFileAttributes.fileType == EFFileAttributes.BINARY_FILE_TYPE ||
                efFileAttributes.fileType == EFFileAttributes.BINARY_FILE_TRANS_TYPE) {
            fileAttributeBytes = ByteUtils.intToBytes(efFileAttributes.fileSize, 2);
        } else {
            fileAttributeBytes[0] = (byte) efFileAttributes.numOfRecs;
            fileAttributeBytes[1] = (byte) efFileAttributes.RecSize;
        }

        System.arraycopy(fileAttributeBytes, 0, responseBytes, fileDataAttributeIndex, fileAttributeBytes.length);
        responseBytes[nbrKIndex] = (byte) efFileAttributes.numOfKeys;
        System.arraycopy(smr.getSMRBytes(), 0, responseBytes, smrIndex, 2);
        System.arraycopy(artArrayToBytes(), 0, responseBytes, artIndex, arts.length);
        return responseBytes;
    }

    private byte[] artArrayToBytes() {
        byte[] artBytes = new byte[arts.length];

        for (int i = 0; i < arts.length; i++) {
            artBytes[i] = arts[i].getACGValue();
        }
        ;
        return artBytes;
    }

    public byte[] updateFileAttributes(CommandApdu commandApdu) {
        byte[] data = commandApdu.getData();

        int numOfKeyIndex = 0;
        int smrIndex = numOfKeyIndex + 1;
        int artIndex = smrIndex + 2;

        byte[] smrData = new byte[2];
        System.arraycopy(data, smrIndex, smrData, 0, smrData.length);

        efFileAttributes.numOfKeys = ByteUtils.byteToShort(data[numOfKeyIndex]);
        arts = new ART[efFileAttributes.numOfKeys + 1];
        byte[] artBytes = new byte[efFileAttributes.numOfKeys + 1];
        System.arraycopy(data, artIndex, artBytes, 0, artBytes.length);

        for (int i = 0; i < arts.length; i++) {
            int acgValue = artBytes[i];
            arts[i] = new ART(acgValue);
        }
        smr = new SMR(new ByteArray(smrData));
        return new byte[0];
    }


    protected int getResponseLength(CommandApdu commandApdu) {
        int responseLength;
        if (commandApdu.getLe() < efFileAttributes.fileSize) {
            responseLength = commandApdu.getLe();
        } else {
            responseLength = efFileAttributes.fileSize;
        }
        return responseLength;
    }

    protected int getOffset(int p1, int p2) {
        int offset;
        if (ByteUtils.matchBitByBitIndex((byte) p1, 7)) {
            offset = p2;
        } else {
            byte[] offsetBytes = new byte[2];
            offsetBytes[0] = (byte) (p1 & 0x7F);
            offsetBytes[1] = (byte) p2;
            offset = ByteUtils.byteArrayToInt(offsetBytes);
        }
        return offset;
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
        efFileAttributes.fileType = ByteUtils.byteToShort(data[fileTypeIndex]);
        efFileAttributes.SFID = ByteUtils.byteToShort(data[sfidIndex]);
        efFileAttributes.fileID = ByteUtils.getShort(data, (short) fileIDIndex);
        byte[] fileAttributes = new byte[2];
        System.arraycopy(data, fileAttributeIndex, fileAttributes, 0, fileAttributes.length);
        efFileAttributes.numOfRecs = 0;
        efFileAttributes.fileSize = ByteUtils.byteToInt(fileAttributes[1]);
        efFileAttributes.numOfKeys = ByteUtils.byteToShort(data[numOfKeyIndex]);

        byte[] smrData = new byte[2];
        System.arraycopy(data, smrIndex, smrData, 0, smrData.length);
        byte[] artBytes = new byte[efFileAttributes.numOfKeys + 1];

        arts = new ART[efFileAttributes.numOfKeys + 1];
        System.arraycopy(data, artIndex, artBytes, 0, artBytes.length);

        for (int i = 0; i < arts.length; i++) {
            int acgValue = artBytes[i];
            arts[i] = new ART(acgValue);
        }
        smr = new SMR(new ByteArray(smrData));
        return new byte[0];
    }


    public EFFileAttributes getEfFileAttributes() {
        return efFileAttributes;
    }

    public SMR getSmr() {
        return smr;
    }

    public ART[] getArts() {
        return arts;
    }
}
