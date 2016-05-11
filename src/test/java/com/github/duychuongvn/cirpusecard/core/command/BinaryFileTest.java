package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.command.impl.ADFFileImpl;
import com.github.duychuongvn.cirpusecard.core.command.impl.BinaryFileImpl;
import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.osptalliance.cipurse.commands.EFFileAttributes;

/**
 * Created by huynhduychuong on 5/5/2016.
 */
public class BinaryFileTest {

    @Test
    public void shouldReadCreateEFCommand() {

        ADFFile adfFile = new ADFFileImpl();
        BinaryFileImpl binaryFile = new BinaryFileImpl(adfFile);
        String command = "92 01 0C 11 00 30 00 00 64 02 06 00 7F FF FF";
        binaryFile.createFile(new CommandApdu(CommandEnum.CREATE_FILE, 00, 00, ByteUtils.fromHexString(command)));
        Assertions.assertThat(binaryFile.getEfFileAttributes().fileSize).isEqualTo(100);
        Assertions.assertThat(binaryFile.getEfFileAttributes().fileType).isEqualTo(EFFileAttributes.BINARY_FILE_TRANS_TYPE);
        Assertions.assertThat(binaryFile.getEfFileAttributes().fileID).isEqualTo(ByteUtils.byteArrayToInt(new byte[]{(byte) 0x30, (byte) 0x00}));
        Assertions.assertThat(binaryFile.getEfFileAttributes().numOfKeys).isEqualTo((short) 2);
        Assertions.assertThat(binaryFile.getEfFileAttributes().SFID).isEqualTo((short) 00);
        Assertions.assertThat(binaryFile.getSmr().SMGroup_1).isEqualTo((byte)0x00);
        Assertions.assertThat(binaryFile.getSmr().SMGroup_2).isEqualTo((byte)0x06);
        Assertions.assertThat(binaryFile.getSmr().SMGroup_3).isEqualTo((byte)0x00);
        Assertions.assertThat(binaryFile.getSmr().SMGroup_4).isEqualTo((byte)0x00);

        Assertions.assertThat(binaryFile.getArts()[0].ACGroup_1).isTrue();
        Assertions.assertThat(binaryFile.getArts()[0].ACGroup_2).isTrue();
        Assertions.assertThat(binaryFile.getArts()[0].ACGroup_3).isTrue();
        Assertions.assertThat(binaryFile.getArts()[0].ACGroup_4).isTrue();
        Assertions.assertThat(binaryFile.getArts()[0].ACGroup_5).isTrue();
        Assertions.assertThat(binaryFile.getArts()[0].ACGroup_6).isTrue();
        Assertions.assertThat(binaryFile.getArts()[0].ACGroup_7).isTrue();
        Assertions.assertThat(binaryFile.getArts()[0].ACGroup_8).isFalse();

        Assertions.assertThat(binaryFile.getArts()[1].ACGroup_1).isTrue();
        Assertions.assertThat(binaryFile.getArts()[1].ACGroup_2).isTrue();
        Assertions.assertThat(binaryFile.getArts()[1].ACGroup_3).isTrue();
        Assertions.assertThat(binaryFile.getArts()[1].ACGroup_4).isTrue();
        Assertions.assertThat(binaryFile.getArts()[1].ACGroup_5).isTrue();
        Assertions.assertThat(binaryFile.getArts()[1].ACGroup_6).isTrue();
        Assertions.assertThat(binaryFile.getArts()[1].ACGroup_7).isTrue();
        Assertions.assertThat(binaryFile.getArts()[1].ACGroup_8).isTrue();

        Assertions.assertThat(binaryFile.getArts()[2].ACGroup_1).isTrue();
        Assertions.assertThat(binaryFile.getArts()[2].ACGroup_2).isTrue();
        Assertions.assertThat(binaryFile.getArts()[2].ACGroup_3).isTrue();
        Assertions.assertThat(binaryFile.getArts()[2].ACGroup_4).isTrue();
        Assertions.assertThat(binaryFile.getArts()[2].ACGroup_5).isTrue();
        Assertions.assertThat(binaryFile.getArts()[2].ACGroup_6).isTrue();
        Assertions.assertThat(binaryFile.getArts()[2].ACGroup_7).isTrue();
        Assertions.assertThat(binaryFile.getArts()[2].ACGroup_8).isTrue();

    }

    @Test
    public void shouldUpdateBinaryAndReadSuccess() {
        ADFFile adfFile = new ADFFileImpl();
        BinaryFileImpl binaryFile = new BinaryFileImpl(adfFile);
        String command = "92 01 0C 11 00 30 00 00 64 02 06 00 7F FF FF";
        binaryFile.createFile(new CommandApdu(CommandEnum.CREATE_FILE, 00, 00, ByteUtils.fromHexString(command)));

        String updateBinary = "01 02 03 04";
        binaryFile.updateBinary(new CommandApdu(CommandEnum.UPDATE_BINARY, 0, 0, ByteUtils.fromHexString(updateBinary)));
        byte[] fileData = binaryFile.readBinary(new CommandApdu(CommandEnum.READ_BINARY, 0 ,0, new byte[]{}, 256));
        byte[] expectedData = new byte[100];
        expectedData[0] = (byte)0x01;
        expectedData[1] = (byte)0x02;
        expectedData[2] = (byte)0x03;
        expectedData[3] = (byte)0x04;
        Assertions.assertThat(fileData).isEqualTo(expectedData);
    }
}
