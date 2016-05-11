package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.command.impl.ADFFileImpl;
import com.github.duychuongvn.cirpusecard.core.command.impl.BinaryFileImpl;
import com.github.duychuongvn.cirpusecard.core.command.impl.CyclicRecordFileImpl;
import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.osptalliance.cipurse.commands.ART;
import org.osptalliance.cipurse.commands.EFFileAttributes;

/**
 * Created by huynhduychuong on 5/11/2016.
 */
public class CyclicRecordFileTest {

    @Test
    public void shouldCreateCyclicRecordFileSuccessful() {
        String commandData =  "92 01 0D 06 00 30 01 05 64 03 5A 5A FF FF FF FF";
        ADFFile adfFile = new ADFFileImpl();
        CyclicRecordFileImpl cyclicRecordFile = new CyclicRecordFileImpl(adfFile);
        cyclicRecordFile.createFile(new CommandApdu(CommandEnum.CREATE_FILE, 00, 00, ByteUtils.fromHexString(commandData)));
        Assertions.assertThat(cyclicRecordFile.getEfFileAttributes().fileSize).isEqualTo(100);
        Assertions.assertThat(cyclicRecordFile.getEfFileAttributes().fileType).isEqualTo(EFFileAttributes.CYCLIC_RECORD_TYPE);
        Assertions.assertThat(cyclicRecordFile.getEfFileAttributes().fileID).isEqualTo(ByteUtils.byteArrayToInt(new byte[]{(byte) 0x30, (byte) 0x01}));
        Assertions.assertThat(cyclicRecordFile.getEfFileAttributes().numOfKeys).isEqualTo((short) 3);
        Assertions.assertThat(cyclicRecordFile.getEfFileAttributes().SFID).isEqualTo((short) 00);
        Assertions.assertThat(cyclicRecordFile.getEfFileAttributes().RecSize).isEqualTo((short) 100);
        Assertions.assertThat(cyclicRecordFile.getEfFileAttributes().numOfRecs).isEqualTo((short) 5);
        Assertions.assertThat(cyclicRecordFile.getSmr().SMGroup_1).isEqualTo((byte)0x05);
        Assertions.assertThat(cyclicRecordFile.getSmr().SMGroup_2).isEqualTo((byte)0x0A);
        Assertions.assertThat(cyclicRecordFile.getSmr().SMGroup_3).isEqualTo((byte)0x05);
        Assertions.assertThat(cyclicRecordFile.getSmr().SMGroup_4).isEqualTo((byte)0x0A);

        ART art0 = cyclicRecordFile.getArts()[0];
        ART art1 = cyclicRecordFile.getArts()[1];
        ART art2 = cyclicRecordFile.getArts()[2];
        ART art3 = cyclicRecordFile.getArts()[3];
        Assertions.assertThat(art0.ACGroup_1).isTrue();
        Assertions.assertThat(art0.ACGroup_2).isTrue();
        Assertions.assertThat(art0.ACGroup_3).isTrue();
        Assertions.assertThat(art0.ACGroup_4).isTrue();
        Assertions.assertThat(art0.ACGroup_5).isTrue();
        Assertions.assertThat(art0.ACGroup_6).isTrue();
        Assertions.assertThat(art0.ACGroup_7).isTrue();
        Assertions.assertThat(art0.ACGroup_8).isTrue();

        Assertions.assertThat(art1.ACGroup_1).isTrue();
        Assertions.assertThat(art1.ACGroup_2).isTrue();
        Assertions.assertThat(art1.ACGroup_3).isTrue();
        Assertions.assertThat(art1.ACGroup_4).isTrue();
        Assertions.assertThat(art1.ACGroup_5).isTrue();
        Assertions.assertThat(art1.ACGroup_6).isTrue();
        Assertions.assertThat(art1.ACGroup_7).isTrue();
        Assertions.assertThat(art1.ACGroup_8).isTrue();

        Assertions.assertThat(art2.ACGroup_1).isTrue();
        Assertions.assertThat(art2.ACGroup_2).isTrue();
        Assertions.assertThat(art2.ACGroup_3).isTrue();
        Assertions.assertThat(art2.ACGroup_4).isTrue();
        Assertions.assertThat(art2.ACGroup_5).isTrue();
        Assertions.assertThat(art2.ACGroup_6).isTrue();
        Assertions.assertThat(art2.ACGroup_7).isTrue();
        Assertions.assertThat(art2.ACGroup_8).isTrue();

        Assertions.assertThat(art3.ACGroup_1).isTrue();
        Assertions.assertThat(art3.ACGroup_2).isTrue();
        Assertions.assertThat(art3.ACGroup_3).isTrue();
        Assertions.assertThat(art3.ACGroup_4).isTrue();
        Assertions.assertThat(art3.ACGroup_5).isTrue();
        Assertions.assertThat(art3.ACGroup_6).isTrue();
        Assertions.assertThat(art3.ACGroup_7).isTrue();
        Assertions.assertThat(art3.ACGroup_8).isTrue();
    }
}
