package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by huynhduychuong on 5/11/2016.
 */
public class CreateCipurseFileFactoryTest {

    @Test
    public void shouldCreateADF() {
        CipurseFile adfFile = CreateCipurseFileFactory.createInstance(new CommandApdu(CommandEnum.CREATE_FILE, 0, 0, ByteUtils.fromHexString("92 00 27 38 20 5F 00 01 01 03 00 06 40 00 00 13 02 10 09 02 10 09 02 10 09 62 0F 84 0D D2 76 00 00 04 15 02 00 00 03 00 01 01 A0 0F 3C 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B")), null);
        Assertions.assertThat(adfFile).isInstanceOf(ADFFile.class);
    }

    @Test
    public void shouldCreateADFThenCreateBinaryFile() {
        CipurseFile adfFile = CreateCipurseFileFactory.createInstance(new CommandApdu(CommandEnum.CREATE_FILE, 0, 0, ByteUtils.fromHexString("92 00 27 38 20 5F 00 01 01 03 00 06 40 00 00 13 02 10 09 02 10 09 02 10 09 62 0F 84 0D D2 76 00 00 04 15 02 00 00 03 00 01 01 A0 0F 3C 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B")), null);
        CipurseFile binaryFile = CreateCipurseFileFactory.createInstance(new CommandApdu(CommandEnum.CREATE_FILE, 0, 0, ByteUtils.fromHexString("92 01 0C 11 00 30 00 00 64 02 06 00 7F FF FF")), (ADFFile) adfFile);
        Assertions.assertThat(binaryFile).isInstanceOf(BinaryFile.class);
    }
}
