package com.github.duychuongvn.cirpusecard.core.command.impl;

import com.github.duychuongvn.cirpusecard.core.command.*;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.osptalliance.cipurse.commands.*;

import java.util.Arrays;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public class ADFFileImpl implements ADFFile {
    private DFFileAttributes dfFileAttributes = new DFFileAttributes();
    private SecurityAttributes securityAttributes;
    private SMR smr;
    private ART[] art;
    private KeyAttributeInfo[] keyAttributeInfos;


    private ElementFile currentEF;
    private ElementFile[] efFiles;

    public ADFFileImpl() {

    }

    public byte[] deactiveFile(CommandApdu commandApdu) {
        return new byte[0];
    }

    public byte[] activeFile(CommandApdu commandApdu) {
        return new byte[0];
    }

    public byte[] updateKey(CommandApdu commandApdu) {
        return new byte[0];
    }

    public byte[] updateKeyAttributes(CommandApdu commandApdu) {
        return new byte[0];
    }

    public byte[] createEF(CommandApdu commandApdu) {
        currentEF = ElementFileFactory.createInstance(commandApdu, this);
        currentEF.createFile(commandApdu);
        pushCurrentEFIntoCache();
        return new byte[0];
    }

    public byte[] selectEF(CommandApdu commandApdu) {
        int fidIndex = 0;
        byte[] fidBytes = new byte[commandApdu.getLc()];

        System.arraycopy(commandApdu.getData(), fidIndex, fidBytes, 0,  commandApdu.getLc());
        int fid = ByteUtils.byteArrayToInt(fidBytes);
        boolean isFileSelected = false;
        for (ElementFile elementFile : efFiles) {
            if (elementFile.getEfFileAttributes().fileID == fid) {
                currentEF = elementFile;
                isFileSelected = true;
                break;
            }
        }
        if (!isFileSelected) {
            //TODO: throw exception
        }
        return new byte[0];
    }

    public byte[] executeEFCommand(CommandApdu commandApdu) {
        return currentEF.execute(commandApdu);
    }

    private void pushCurrentEFIntoCache() {
        for (int i = 0; i < efFiles.length; i++) {
            if (efFiles[i] == null) {
                efFiles[i] = currentEF;
                break;
            }
        }
    }

    public byte[] createFile(CommandApdu commandApdu) {
        byte[] data = commandApdu.getData();
        // FE-Len- FD: 1 byte - Type: 1 byte - FileId -NbrEF-NbrSFID-NbrKey-SMR-ART-KeySet-FCP
        // 92 00 27 38 20 5F 00 01 01 03 00 06 40 00 00 13 02 10 09 02 10 09 02 10 09 62 0F 84 0D D2 76 00 00 04 15 02 00 00 03 00 01 01 A0 0F 3C 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B

        byte[] secAttributes = new byte[data[2]];
        int fdIndex = 0;
        int typeIndex = fdIndex + 1;
        int fileIdIndex = typeIndex + 1;
        int numOfEfsIndex = fileIdIndex + 2;
        int numOfSFIDsIndex = numOfEfsIndex + 1;
        int numOfKeysIndex = numOfSFIDsIndex + 1;
        int smrIndex = numOfKeysIndex + 1;
        int artIndex = smrIndex + 2;

        System.arraycopy(data, 3, secAttributes, 0, secAttributes.length);
        dfFileAttributes.setsecAttrValue(new ByteArray(secAttributes));
        dfFileAttributes.fileDescriptor = ByteUtils.byteToShort(secAttributes[fdIndex]);
        dfFileAttributes.appProfile = (byte) (secAttributes[typeIndex] >> 5);
        dfFileAttributes.fileID = ByteUtils.getShort(secAttributes, (short) fileIdIndex);
        dfFileAttributes.numOfEFs = ByteUtils.byteToShort(secAttributes[numOfEfsIndex]);
        dfFileAttributes.numOfSFIDs = ByteUtils.byteToShort(secAttributes[numOfSFIDsIndex]);
        dfFileAttributes.numOfKeys = ByteUtils.byteToShort(secAttributes[numOfKeysIndex]);

        int keySetIndex = artIndex + dfFileAttributes.numOfKeys + 1;
        int fcpIndex = keySetIndex + dfFileAttributes.numOfKeys * 3;

        int tagLength = secAttributes.length - fcpIndex;
        byte[] tag62 = new byte[tagLength];
        System.arraycopy(secAttributes, fcpIndex, tag62, 0, tagLength);
        this.smr = new SMR(new ByteArray(new byte[]{secAttributes[smrIndex], secAttributes[smrIndex + 1]}));

        byte[] artBytes = new byte[dfFileAttributes.numOfKeys + 1];
        art = new ART[dfFileAttributes.numOfKeys + 1];
        System.arraycopy(secAttributes, artIndex, artBytes, 0, artBytes.length);

        for (int i = 0; i < art.length; i++) {
            int acgValue = artBytes[i];
            art[i] = new ART(acgValue);
        }

        byte[] keySetInBytes = new byte[dfFileAttributes.numOfKeys * 3];
        System.arraycopy(secAttributes, keySetIndex, keySetInBytes, 0, keySetInBytes.length);
        int keyValueLength = data.length - 3 - secAttributes.length;
        boolean isContainsKey = keyValueLength > 0;
        byte[] keyValueBytes = new byte[keyValueLength];
        System.arraycopy(data, secAttributes.length + 3, keyValueBytes, 0, keyValueBytes.length);

        // A0 0F 3C 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B
        int keysLength = keyValueBytes[2];
        if (keysLength % 20 != 0) {
            throw new IllegalArgumentException("Key length not valid");
        }
        securityAttributes = new SecurityAttributes(dfFileAttributes.numOfKeys, smr, art);
        keyAttributeInfos = new KeyAttributeInfo[dfFileAttributes.numOfKeys];
        int keyIndex = 3;
        for (int i = 0; i < dfFileAttributes.numOfKeys; i++) {

            byte[] keySet = new byte[3];
            System.arraycopy(keySetInBytes, i * 3, keySet, 0, 3);
            byte[] keyDataBytes = new byte[KeyAttributeInfo.KEY_OBJECT_SIZE];
            System.arraycopy(keyValueBytes, keyIndex, keyDataBytes, 0, keyDataBytes.length);
            keyIndex += keyDataBytes.length;
            KeyAttributeInfo keyAttributeInfo = new KeyAttributeInfo();
            keyAttributeInfo.setKeyValue(new ByteArray(Arrays.copyOf(keyDataBytes, 16)));
            keyAttributeInfo.kvv = Arrays.copyOfRange(keyDataBytes, 17, 20);
            byte keyInfo = keyDataBytes[16];
            keyAttributeInfo.keySecAttrib = keySet[0];
            keyAttributeInfo.keyLength = keySet[1];
            keyAttributeInfo.keyAddInfo = keySet[2];
            keyAttributeInfos[i] = keyAttributeInfo;
        }

        efFiles = new ElementFile[dfFileAttributes.numOfEFs];
        return new byte[0];
    }

    public byte[] readFileAttributes(CommandApdu commandApdu) {
        return new byte[0];
    }

    public byte[] updateFileAttributes(CommandApdu commandApdu) {
        return new byte[0];
    }


    public DFFileAttributes getDfFileAttributes() {
        return dfFileAttributes;
    }

    public SMR getSmr() {
        return smr;
    }

    public ART[] getArt() {
        return art;
    }

    public KeyAttributeInfo[] getKeyAttributeInfos() {
        return keyAttributeInfos;
    }

    public SecurityAttributes getSecurityAttributes() {
        return securityAttributes;
    }

    public ElementFile getCurrentEF() {
        return currentEF;
    }

}
