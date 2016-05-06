package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.command.impl.ADFFileImpl;
import com.github.duychuongvn.cirpusecard.core.command.impl.BinaryFileImpl;
import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import com.github.duychuongvn.cirpusecard.core.util.CommandApdu;
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

        Assertions.assertThat(binaryFile.getArt()[0].ACGroup_1).isTrue();
        Assertions.assertThat(binaryFile.getArt()[0].ACGroup_2).isTrue();
        Assertions.assertThat(binaryFile.getArt()[0].ACGroup_3).isTrue();
        Assertions.assertThat(binaryFile.getArt()[0].ACGroup_4).isTrue();
        Assertions.assertThat(binaryFile.getArt()[0].ACGroup_5).isTrue();
        Assertions.assertThat(binaryFile.getArt()[0].ACGroup_6).isTrue();
        Assertions.assertThat(binaryFile.getArt()[0].ACGroup_7).isTrue();
        Assertions.assertThat(binaryFile.getArt()[0].ACGroup_8).isFalse();

        Assertions.assertThat(binaryFile.getArt()[1].ACGroup_1).isTrue();
        Assertions.assertThat(binaryFile.getArt()[1].ACGroup_2).isTrue();
        Assertions.assertThat(binaryFile.getArt()[1].ACGroup_3).isTrue();
        Assertions.assertThat(binaryFile.getArt()[1].ACGroup_4).isTrue();
        Assertions.assertThat(binaryFile.getArt()[1].ACGroup_5).isTrue();
        Assertions.assertThat(binaryFile.getArt()[1].ACGroup_6).isTrue();
        Assertions.assertThat(binaryFile.getArt()[1].ACGroup_7).isTrue();
        Assertions.assertThat(binaryFile.getArt()[1].ACGroup_8).isTrue();

        Assertions.assertThat(binaryFile.getArt()[2].ACGroup_1).isTrue();
        Assertions.assertThat(binaryFile.getArt()[2].ACGroup_2).isTrue();
        Assertions.assertThat(binaryFile.getArt()[2].ACGroup_3).isTrue();
        Assertions.assertThat(binaryFile.getArt()[2].ACGroup_4).isTrue();
        Assertions.assertThat(binaryFile.getArt()[2].ACGroup_5).isTrue();
        Assertions.assertThat(binaryFile.getArt()[2].ACGroup_6).isTrue();
        Assertions.assertThat(binaryFile.getArt()[2].ACGroup_7).isTrue();
        Assertions.assertThat(binaryFile.getArt()[2].ACGroup_8).isTrue();

    }
}
