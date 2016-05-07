package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by huynhduychuong on 5/7/2016.
 */
public class CommandFactoryTest {
    @Test
    public void shouldCreateADFThenCreateEFThenUpdateBinaryThenReadBinarySuccessful() {
        String createADFCommand = "00 E0 00 00 69 92 00 27 38 20 5F 00 01 01 03 00 06 40 00 00 13 02 10 09 02 10 09 02 10 09 62 0F 84 0D D2 76 00 00 04 15 02 00 00 03 00 01 01 A0 0F 3C 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 01 5F D6 7B";
        String createEFCommand = "00  E0 00 00 0F 92 01 0C 11 00 30 00 00 64 02 06 00 7F FF FF";
        String updateBinaryCommand = "00 D6 00 00 04 01 02 03 04";
        String readBinaryCommand = "00 B0 00 00 C8";

        CommandFactory commandFactory = new CommandFactory();
        String createADFResponse = ByteUtils.bytesToHexString(commandFactory.execute(ByteUtils.fromHexString(createADFCommand)));
        String createEFResponse = ByteUtils.bytesToHexString(commandFactory.execute(ByteUtils.fromHexString(createEFCommand)));
        String updateBinaryResponse = ByteUtils.bytesToHexString(commandFactory.execute(ByteUtils.fromHexString(updateBinaryCommand)));
        String readBinaryResponse = ByteUtils.bytesToHexString(commandFactory.execute(ByteUtils.fromHexString(readBinaryCommand)));

        byte[] expectedReadBinaryResponse = new byte[102];
        expectedReadBinaryResponse[0] = 1;
        expectedReadBinaryResponse[1] = 2;
        expectedReadBinaryResponse[2] = 3;
        expectedReadBinaryResponse[3] = 4;
        expectedReadBinaryResponse[100] = (byte)0x90;
        expectedReadBinaryResponse[101] = (byte)0x00;

        Assertions.assertThat(createADFResponse).isEqualTo("90 00");
        Assertions.assertThat(createEFResponse).isEqualTo("90 00");
        Assertions.assertThat(updateBinaryResponse).isEqualTo("90 00");
        Assertions.assertThat(readBinaryResponse).isEqualTo(ByteUtils.bytesToHexString(expectedReadBinaryResponse));
    }

    public static void main(String[] args) {
        byte d = -56;
        System.out.println(d & 255);
    }
}
