package com.github.duychuongvn.cirpusecard.core.util;

import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.sun.prism.impl.paint.PaintUtil;
import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by huynhduychuong on 4/26/2016.
 */
public class CommandApduTest {


    @Test
    public void shouldReturnBytesContainHeadersAndLen() {

        String actualCommand = ByteUtils.bytesToHexString(new CommandApdu(CommandEnum.SELECT, 0x04, 0x00, new byte[]{(byte) 0xD0, (byte) 0x00, (byte) 0x00, (byte) 0x01}).toBytes());
        Assertions.assertThat(actualCommand).isEqualTo("00 A4 04 00 04 D0 00 00 01");

    }
    @Test
    public void shouldReturnBytesContainHeadersAndLenAndLe() {

        String actualCommand = ByteUtils.bytesToHexString(new CommandApdu(CommandEnum.APPEND_RECORD, 0x01, 0x00, ByteUtils.fromHexString("31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30"), 0x0102).toBytes());
        Assertions.assertThat(actualCommand).isEqualTo("00 E2 01 00 00 01 2C 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 01 02");

    }

    @Test
    public void shouldParseCommandCase1() {
        String command = "00 A4 00 00";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        Assertions.assertThat(commandApdu.getCommandEnum()).isEqualTo(CommandEnum.SELECT);
        Assertions.assertThat(commandApdu.getCla()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getIns()).isEqualTo(0xA4);
        Assertions.assertThat(commandApdu.getP1()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getP2()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getData()).isEqualTo(new byte[]{});
        Assertions.assertThat(commandApdu.getLc()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getLe()).isEqualTo(0x00);
    }

    @Test
    public void shouldParseCommandCase2() {
        String command = "00 A4 04 01 05";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        Assertions.assertThat(commandApdu.getCommandEnum()).isEqualTo(CommandEnum.SELECT);
        Assertions.assertThat(commandApdu.getCla()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getIns()).isEqualTo(0xA4);
        Assertions.assertThat(commandApdu.getP1()).isEqualTo(0x04);
        Assertions.assertThat(commandApdu.getP2()).isEqualTo(0x01);
        Assertions.assertThat(commandApdu.getData()).isEqualTo(new byte[]{});
        Assertions.assertThat(commandApdu.getLc()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getLe()).isEqualTo(0x05);
        Assertions.assertThat(commandApdu.isLeUsed()).isTrue();
    }
    @Test
    public void shouldParseCommandCase2AndLeIs256() {
        String command = "00 A4 04 01 00";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        Assertions.assertThat(commandApdu.getCommandEnum()).isEqualTo(CommandEnum.SELECT);
        Assertions.assertThat(commandApdu.getCla()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getIns()).isEqualTo(0xA4);
        Assertions.assertThat(commandApdu.getP1()).isEqualTo(0x04);
        Assertions.assertThat(commandApdu.getP2()).isEqualTo(0x01);
        Assertions.assertThat(commandApdu.getData()).isEqualTo(new byte[]{});
        Assertions.assertThat(commandApdu.getLc()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getLe()).isEqualTo(256);
        Assertions.assertThat(commandApdu.isLeUsed()).isTrue();
    }
    @Test
    public void shouldParseCommandCase2Extended() {
        String command = "00 A4 04 01 00 14 02";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        Assertions.assertThat(commandApdu.getCommandEnum()).isEqualTo(CommandEnum.SELECT);
        Assertions.assertThat(commandApdu.getCla()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getIns()).isEqualTo(0xA4);
        Assertions.assertThat(commandApdu.getP1()).isEqualTo(0x04);
        Assertions.assertThat(commandApdu.getP2()).isEqualTo(0x01);
        Assertions.assertThat(commandApdu.getData()).isEqualTo(new byte[]{});
        Assertions.assertThat(commandApdu.getLc()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getLe()).isEqualTo(5122);
        Assertions.assertThat(commandApdu.isLeUsed()).isTrue();
    }

    @Test
    public void shouldParseCommandCase3() {

        String command = "00 A4 04 00 04 D0 00 00 01";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        Assertions.assertThat(commandApdu.getCommandEnum()).isEqualTo(CommandEnum.SELECT);
        Assertions.assertThat(commandApdu.getCla()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getIns()).isEqualTo(0xA4);
        Assertions.assertThat(commandApdu.getP1()).isEqualTo(0x04);
        Assertions.assertThat(commandApdu.getP2()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getData()).isEqualTo(new byte[]{(byte) 0xD0, (byte) 0x00, (byte) 0x00, (byte) 0x01});
        Assertions.assertThat(commandApdu.getLc()).isEqualTo(0x04);
        Assertions.assertThat(commandApdu.getLe()).isEqualTo(0x00);
    }

    @Test
    public void shouldParseCommandCase3Extended() {

        String command = "00 A4 04 00 00 00 05 D0 00 00 01 02";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        Assertions.assertThat(commandApdu.getCommandEnum()).isEqualTo(CommandEnum.SELECT);
        Assertions.assertThat(commandApdu.getCla()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getIns()).isEqualTo(0xA4);
        Assertions.assertThat(commandApdu.getP1()).isEqualTo(0x04);
        Assertions.assertThat(commandApdu.getP2()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getData()).isEqualTo(new byte[]{(byte) 0xD0, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x02});
        Assertions.assertThat(commandApdu.getLc()).isEqualTo(0x05);
        Assertions.assertThat(commandApdu.getLe()).isEqualTo(0x00);
    }

    @Test
    public void shouldParseCommandCase4() {

        String command = "00 A4 04 00 05 D0 00 00 01 02 06";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        Assertions.assertThat(commandApdu.getCommandEnum()).isEqualTo(CommandEnum.SELECT);
        Assertions.assertThat(commandApdu.getCla()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getIns()).isEqualTo(0xA4);
        Assertions.assertThat(commandApdu.getP1()).isEqualTo(0x04);
        Assertions.assertThat(commandApdu.getP2()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getData()).isEqualTo(new byte[]{(byte) 0xD0, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x02});
        Assertions.assertThat(commandApdu.getLc()).isEqualTo(0x05);
        Assertions.assertThat(commandApdu.getLe()).isEqualTo(0x06);
        Assertions.assertThat(commandApdu.isLeUsed()).isTrue();
    }

    @Test
    public void shouldParseCommandCase4Extended() {

        String command = "00 A4 04 00 00 00 05 D0 00 00 01 02 02 00";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        Assertions.assertThat(commandApdu.getCommandEnum()).isEqualTo(CommandEnum.SELECT);
        Assertions.assertThat(commandApdu.getCla()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getIns()).isEqualTo(0xA4);
        Assertions.assertThat(commandApdu.getP1()).isEqualTo(0x04);
        Assertions.assertThat(commandApdu.getP2()).isEqualTo(0x00);
        Assertions.assertThat(commandApdu.getData()).isEqualTo(new byte[]{(byte) 0xD0, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x02});
        Assertions.assertThat(commandApdu.getLc()).isEqualTo(0x05);
        Assertions.assertThat(commandApdu.getLe()).isEqualTo(512);
        Assertions.assertThat(commandApdu.isLeUsed()).isTrue();
    }

    @Test
    public void shouldConvertToBytes() {
        String command = "00 A4 04 00 05 D0 00 00 01 02";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        String actualBytes = ByteUtils.bytesToHexString(commandApdu.toBytes());
        Assertions.assertThat(actualBytes).isEqualTo(command);
    }
    @Test
    public void shouldConvertToBytesLe() {
        String command = "00 A4 04 00 05";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        String actualBytes = ByteUtils.bytesToHexString(commandApdu.toBytes());
        Assertions.assertThat(actualBytes).isEqualTo(command);
    }
    @Test
    public void shouldConvertToBytesLe00() {
        String command = "00 A4 04 00 00";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        String actualBytes = ByteUtils.bytesToHexString(commandApdu.toBytes());
        Assertions.assertThat(actualBytes).isEqualTo(command);
    }
    @Test
    public void shouldConvertToBytesLeGreaterThan256() {
        String command = "00 A4 04 00 00 01 01";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        String actualBytes = ByteUtils.bytesToHexString(commandApdu.toBytes());
        Assertions.assertThat(actualBytes).isEqualTo(command);
    }
    @Test
    public void shouldConvertToBytesLeAndLc() {
        String command = "00 A4 04 00 05 D0 00 00 01 02 05";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        String actualBytes = ByteUtils.bytesToHexString(commandApdu.toBytes());
        Assertions.assertThat(actualBytes).isEqualTo(command);
    }
    @Test
    public void shouldConvertToBytesLeAndLcGreaterThan256() {
        String command = "00 A4 04 00 00 01 2C 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 01 03";
        CommandApdu commandApdu = new CommandApdu(ByteUtils.fromHexString(command));
        String actualBytes = ByteUtils.bytesToHexString(commandApdu.toBytes());
        Assertions.assertThat(actualBytes).isEqualTo(command);
    }
}
