package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.command.impl.ADFFileImpl;
import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import com.github.duychuongvn.cirpusecard.core.util.CommandApdu;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.osptalliance.cipurse.commands.DFFileAttributes;

/**
 * Created by huynhduychuong on 4/29/2016.
 */
public class ADFFileImplTest {
    @Test
    public void shouldReadCreateADFCommand() {

        ADFFileImpl adfFile = new ADFFileImpl();
        String command = "92 00 27 38 20 5F 00 01 01 03 00 06 40 00 00 13 02 10 09 02 10 09 02 10 09 62 0F 84 0D D2 76 00 00 04 15 02 00 00 03 00 01 01 A0 0F 3C 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B";
        adfFile.createFile(new CommandApdu(CommandEnum.CREATE_FILE, 00, 00, ByteUtils.fromHexString(command)));
//        Assertions.assertThat(adfFile.getDfFileAttributes().aidValue).isEqualTo(ByteUtils.fromHexString("D2 76 00 00 04 15 02 00 00 03 00 01 01"));
        Assertions.assertThat(adfFile.getDfFileAttributes().appProfile).isEqualTo(DFFileAttributes.PROFILE_L);
        Assertions.assertThat(adfFile.getDfFileAttributes().fileDescriptor).isEqualTo(DFFileAttributes.ADF_FILE_TYPE);
        Assertions.assertThat(adfFile.getDfFileAttributes().fileID).isEqualTo(ByteUtils.byteArrayToInt(ByteUtils.fromHexString("5F 00")));
        Assertions.assertThat(adfFile.getDfFileAttributes().numOfEFs).isEqualTo((short)1);
        Assertions.assertThat(adfFile.getDfFileAttributes().numOfSFIDs).isEqualTo((short)1);
        Assertions.assertThat(adfFile.getDfFileAttributes().numOfKeys).isEqualTo((short)3);
        Assertions.assertThat(adfFile.getDfFileAttributes().isautoSelect).isFalse();
        Assertions.assertThat(adfFile.getSmr().SMGroup_1).isEqualTo((byte)0x00);
        Assertions.assertThat(adfFile.getSmr().SMGroup_2).isEqualTo((byte)0x00);
        Assertions.assertThat(adfFile.getSmr().SMGroup_3).isEqualTo((byte)0x00);
        Assertions.assertThat(adfFile.getSmr().SMGroup_4).isEqualTo((byte)0x06);
        Assertions.assertThat(adfFile.getArt()[0].ACGroup_1).isFalse();
        Assertions.assertThat(adfFile.getArt()[0].ACGroup_2).isFalse();
        Assertions.assertThat(adfFile.getArt()[0].ACGroup_3).isFalse();
        Assertions.assertThat(adfFile.getArt()[0].ACGroup_4).isFalse();
        Assertions.assertThat(adfFile.getArt()[0].ACGroup_5).isFalse();
        Assertions.assertThat(adfFile.getArt()[0].ACGroup_6).isFalse();
        Assertions.assertThat(adfFile.getArt()[0].ACGroup_7).isTrue();
        Assertions.assertThat(adfFile.getArt()[0].ACGroup_8).isFalse();

        Assertions.assertThat(adfFile.getArt()[1].ACGroup_1).isFalse();
        Assertions.assertThat(adfFile.getArt()[1].ACGroup_2).isFalse();
        Assertions.assertThat(adfFile.getArt()[1].ACGroup_3).isFalse();
        Assertions.assertThat(adfFile.getArt()[1].ACGroup_4).isFalse();
        Assertions.assertThat(adfFile.getArt()[1].ACGroup_5).isFalse();
        Assertions.assertThat(adfFile.getArt()[1].ACGroup_6).isFalse();
        Assertions.assertThat(adfFile.getArt()[1].ACGroup_7).isFalse();
        Assertions.assertThat(adfFile.getArt()[1].ACGroup_8).isFalse();

        Assertions.assertThat(adfFile.getArt()[2].ACGroup_1).isFalse();
        Assertions.assertThat(adfFile.getArt()[2].ACGroup_2).isFalse();
        Assertions.assertThat(adfFile.getArt()[2].ACGroup_3).isFalse();
        Assertions.assertThat(adfFile.getArt()[2].ACGroup_4).isFalse();
        Assertions.assertThat(adfFile.getArt()[2].ACGroup_5).isFalse();
        Assertions.assertThat(adfFile.getArt()[2].ACGroup_6).isFalse();
        Assertions.assertThat(adfFile.getArt()[2].ACGroup_7).isFalse();
        Assertions.assertThat(adfFile.getArt()[2].ACGroup_8).isFalse();

        Assertions.assertThat(adfFile.getArt()[3].ACGroup_1).isTrue();
        Assertions.assertThat(adfFile.getArt()[3].ACGroup_2).isTrue();
        Assertions.assertThat(adfFile.getArt()[3].ACGroup_3).isFalse();
        Assertions.assertThat(adfFile.getArt()[3].ACGroup_4).isFalse();
        Assertions.assertThat(adfFile.getArt()[3].ACGroup_5).isTrue();
        Assertions.assertThat(adfFile.getArt()[3].ACGroup_6).isFalse();
        Assertions.assertThat(adfFile.getArt()[3].ACGroup_7).isFalse();
        Assertions.assertThat(adfFile.getArt()[3].ACGroup_8).isFalse();

        Assertions.assertThat(adfFile.getKeyAttributeInfos()).hasSize(3);
        Assertions.assertThat(adfFile.getKeyAttributeInfos()[0].kvv).isEqualTo(ByteUtils.fromHexString("5F D6 7B"));
        Assertions.assertThat(adfFile.getKeyAttributeInfos()[0].keySecAttrib).isEqualTo((short)2);
        Assertions.assertThat(adfFile.getKeyAttributeInfos()[0].keyLength).isEqualTo((short)16);
        Assertions.assertThat(adfFile.getKeyAttributeInfos()[0].keyAddInfo).isEqualTo((short)9);

    }

    @Test
    public void shouldCreateADFAndStoreEF() {
        ADFFileImpl adfFile = new ADFFileImpl();
        String command = "92 00 27 38 20 5F 00 02 01 03 00 06 40 00 00 13 02 10 09 02 10 09 02 10 09 62 0F 84 0D D2 76 00 00 04 15 02 00 00 03 00 01 01 A0 0F 3C 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B";
        adfFile.createFile(new CommandApdu(CommandEnum.CREATE_FILE, 00, 00, ByteUtils.fromHexString(command)));
        String createEFCommand =  "92 01 0C 11 00 30 00 00 64 02 06 00 7F FF FF";
        adfFile.createEF(new CommandApdu(CommandEnum.CREATE_FILE, 00,00, ByteUtils.fromHexString(createEFCommand)));
        adfFile.createEF(new CommandApdu(CommandEnum.CREATE_FILE, 00,00, ByteUtils.fromHexString(createEFCommand)));
        Assertions.assertThat(adfFile.getCurrentEF()).isNotNull();
    }
}
