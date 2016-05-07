package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huynhduychuong on 5/6/2016.
 */
public class CommandFactory {

    private List<ADFFile> adfFiles = new ArrayList<ADFFile>(10);
    private ADFFile currentADF;

    public byte[] execute(byte[] apdu) {
        CommandApdu commandApdu = new CommandApdu(apdu);
        byte[] response = new byte[0];
        if (commandApdu.getCommandEnum() == CommandEnum.CREATE_FILE) {
            CipurseFile cipurseFile = CreateCipurseFileFactory.createInstance(commandApdu, currentADF);
            if (cipurseFile instanceof ADFFile) {
                this.currentADF = (ADFFile) cipurseFile;
            }
        } else if (commandApdu.getCommandEnum() == CommandEnum.SELECT) {

            switch (commandApdu.getP1()) {
                case (byte) 0x04:
                    response = selectFileByAID(commandApdu);
                    break;
                case (byte) 0x00:
                    response = selectFileByFID(commandApdu);
            }
        } else if (commandApdu.getCommandEnum() == CommandEnum.UPDATE_BINARY
                || commandApdu.getCommandEnum() == CommandEnum.READ_BINARY
                || commandApdu.getCommandEnum() == CommandEnum.READ_RECORD
                || commandApdu.getCommandEnum() == CommandEnum.APPEND_RECORD
                || commandApdu.getCommandEnum() == CommandEnum.UPDATE_RECORD
                || commandApdu.getCommandEnum() == CommandEnum.INCREASE_VALUE
                || commandApdu.getCommandEnum() == CommandEnum.DECREASE_VALUE
                ) {
            response = this.currentADF.executeEFCommand(commandApdu);
        }

        byte[] responseWithStatus = new byte[response.length + 2];
        System.arraycopy(response, 0, responseWithStatus, 0, response.length);
        System.arraycopy(ByteUtils.fromHexString("90 00"), 0, responseWithStatus, response.length, 2);
        return responseWithStatus;
    }

    private byte[] selectFileByFID(CommandApdu commandApdu) {

        currentADF.selectEF(commandApdu);
        return null;
    }

    private byte[] selectFileByAID(CommandApdu commandApdu) {

        int dataIndex = 5;
        int lc = commandApdu.getLc();
        byte[] aidBytes = new byte[lc];
        System.arraycopy(commandApdu.getData(), 5, aidBytes, 0, lc);
        boolean isSelected = false;
        for (ADFFile adfFile : adfFiles) {
            if (adfFile.getDfFileAttributes().aidValue.equals(aidBytes)) {
                currentADF = adfFile;
                isSelected = true;
                break;
            }
        }
        if (!isSelected) {
            // TODO: throw expcetion
        }
        return new byte[0];
    }
}
