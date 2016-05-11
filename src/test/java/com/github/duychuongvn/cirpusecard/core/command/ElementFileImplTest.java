package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.command.impl.ADFFileImpl;
import com.github.duychuongvn.cirpusecard.core.command.impl.BinaryFileImpl;
import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import junit.framework.TestCase;
import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by huynhduychuong on 5/10/2016.
 */
public class ElementFileImplTest {

    @Test
    public void shouldReadFileAttributeSuccessful() {
        ADFFile adfFile = new ADFFileImpl();
        BinaryFileImpl binaryFile = new BinaryFileImpl(adfFile);
        String command = "92 01 0C 11 00 30 00 00 64 02 06 00 7F FF FF";
        binaryFile.createFile(new CommandApdu(CommandEnum.CREATE_FILE, 00, 00, ByteUtils.fromHexString(command)));
        String expectedResponse = "11 00 30 00 00 64 02 06 00 7F FF FF";
        byte[] fileAttributeResponses = binaryFile.readFileAttributes(new CommandApdu(CommandEnum.READ_FILE_ATTRIBUTES, 0, 0, new byte[0]));
        Assertions.assertThat(fileAttributeResponses).isEqualTo(ByteUtils.fromHexString(expectedResponse));

    }
    @Test
    public void shouldUpdateFileAttributeSuccessful() {
        ADFFile adfFile = new ADFFileImpl();
        BinaryFileImpl binaryFile = new BinaryFileImpl(adfFile);
        String command = "92 01 0C 11 00 30 00 00 64 02 06 00 7F FF FF";
        binaryFile.createFile(new CommandApdu(CommandEnum.CREATE_FILE, 00, 00, ByteUtils.fromHexString(command)));

        String expectedResponse = "11 00 30 00 00 64 02 06 00 7F FF FF";
        byte[] fileAttributeResponses = binaryFile.readFileAttributes(new CommandApdu(CommandEnum.READ_FILE_ATTRIBUTES, 0, 0, new byte[0]));
        Assertions.assertThat(fileAttributeResponses).isEqualTo(ByteUtils.fromHexString(expectedResponse));

        String updateFileAttributeCommand = "03 5A 69 E1 E1 E1 E1";
        binaryFile.updateFileAttributes(new CommandApdu(CommandEnum.UPDATE_FILE_ATTRIBUTES, 00, 00, ByteUtils.fromHexString(updateFileAttributeCommand)));

        expectedResponse = "11 00 30 00 00 64 03 5A 69 E1 E1 E1 E1";
        fileAttributeResponses = binaryFile.readFileAttributes(new CommandApdu(CommandEnum.READ_FILE_ATTRIBUTES, 0, 0, new byte[0]));
        Assertions.assertThat(fileAttributeResponses).isEqualTo(ByteUtils.fromHexString(expectedResponse));

    }
}