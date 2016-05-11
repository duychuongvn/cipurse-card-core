package com.github.duychuongvn.cirpusecard.core.command.impl;

import com.github.duychuongvn.cirpusecard.core.command.ADFFile;
import com.github.duychuongvn.cirpusecard.core.command.CommandApdu;
import com.github.duychuongvn.cirpusecard.core.command.ElementFile;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.osptalliance.cipurse.commands.ART;
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

    public abstract byte[] updateFileAttributes(CommandApdu commandApdu);


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
