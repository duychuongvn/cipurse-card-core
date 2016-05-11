package com.github.duychuongvn.cirpusecard.core.util;

import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by huynhduychuong on 4/26/2016.
 */
public class ByteUtilsTest {

    @Test
    public void shouldConvertStringToBytes() {

        String hexSpaceString = "00 A4 04 00";
        String hexString = "00A40400";
        byte[] actualBytesFromHexSpaceString = ByteUtils.fromHexString(hexSpaceString);
        byte[] actualBytesFromHexString = ByteUtils.fromHexString(hexString);
        byte[] expectedBytes = new byte[]{(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00};
        Assertions.assertThat(actualBytesFromHexSpaceString).isEqualTo(expectedBytes);
        Assertions.assertThat(actualBytesFromHexString).isEqualTo(expectedBytes);
    }

    @Test
    public void shouldConvertBytesToString() {

        String expectedHexSpaceString = "00 A4 04 00";
        String actualString = ByteUtils.bytesToHexString(new byte[]{(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00});
        Assertions.assertThat(actualString).isEqualTo(expectedHexSpaceString);
    }

    @Test
    public void shouldConvertBytesToNoSpaceString() {

        String expectedHexSpaceString = "00A40400";
        String actualString = ByteUtils.bytesToHexStringNoSpace(new byte[]{(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00});
        Assertions.assertThat(actualString).isEqualTo(expectedHexSpaceString);
    }

    @Test
    public void shouldConvertBytesToInt() {
        byte[] data = {(byte) 0x01, (byte) 0x02};
        int actualValue = ByteUtils.byteArrayToInt(data);
        Assertions.assertThat(actualValue).isEqualTo(258);
    }

    @Test
    public void shouldConvertIntToByteArray() {

        byte[] expectedBytes = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x02};
        Assertions.assertThat(ByteUtils.toByteArray(258)).isEqualTo(expectedBytes);
    }

    @Test
    public void shouldReturnShortValue() {

        short actualValue = ByteUtils.getShort(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x14, (byte) 0x02}, (short) 2);
        Assertions.assertThat(actualValue).isEqualTo((short) 5122);
        Assertions.assertThat(ByteUtils.byteArrayToInt(new byte[]{(byte) 0x14, (byte) 0x02})).isEqualTo((short) 5122);
    }

    @Test
    public void shouldMatchBitByBitIndex() {
        boolean actual5thBit = ByteUtils.matchBitByBitIndex((byte) 0x10, 4);
        boolean actual4thBit = ByteUtils.matchBitByBitIndex((byte) 0x10, 3);
        Assertions.assertThat(actual5thBit).isTrue();
        Assertions.assertThat(actual4thBit).isFalse();
    }

    @Test
    public void shouldConvertIntToBytes() {

        byte[] expectedOneByte = new byte[]{(byte) 0x0A};
        byte[] expectedTwoBytes = new byte[]{0, 127};
        byte[] expected4Bytes = new byte[]{0, 0, 0, -128};

        Assertions.assertThat(ByteUtils.intToBytes(10, 1)).isEqualTo(expectedOneByte);
        Assertions.assertThat(ByteUtils.intToBytes(127, 2)).isEqualTo(expectedTwoBytes);
        Assertions.assertThat(ByteUtils.intToBytes(128, 4)).isEqualTo(expected4Bytes);
    }
}
