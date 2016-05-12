package com.github.duychuongvn.cirpusecard.core.security.securemessaging;

import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.osptalliance.cipurse.CipurseException;
import org.osptalliance.cipurse.IAes;
import org.osptalliance.cipurse.ICommsChannel;
import org.osptalliance.cipurse.ILogger;
import org.osptalliance.cipurse.commands.ByteArray;
import org.osptalliance.cipurse.commands.CipurseCardHandler;
import org.osptalliance.cipurse.commands.CommandAPI;
import org.osptalliance.cipurse.commands.CommandAPIFactory;

/**
 * Created by huynhduychuong on 5/11/2016.
 */
public class CipurseSecureMessageTest {

    @Test
    public void shouldWrapAndUnwrapENCedCommand() throws CipurseException {

        ICommsChannel commsChannel = new CipurseSecureMessageCommChannel();
        IAes aes = new AES();
        ILogger logger = new Logger();
        CipurseCardHandler cipurseCardHandler = new CipurseCardHandler(commsChannel, aes, logger);
        CommandAPI commandAPI = CommandAPIFactory.getInstance().buildCommandAPI();
        commandAPI.setVersion(CommandAPI.Version.V3);
        byte[] key = ByteUtils.fromHexString("73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73");
        boolean actualResult = cipurseCardHandler.setupSecureChannel((byte) 1, new ByteArray(key));
        Assertions.assertThat(actualResult).isTrue();
        String expectedUnwrapCommand;
        ;
        String actualUnwrappedCommand;

        cipurseCardHandler.secureMessagingIndicator = CipurseCardHandler.SMI_ENC_ENC;
        cipurseCardHandler.secureMessagingFlag = true;
        ByteArray commandNonLe = new ByteArray(ByteUtils.fromHexString("00 A4 04 00 08 5F 00 00 00 00 00 00 01"));
        expectedUnwrapCommand = "01 02 03 04 90 00";
        actualUnwrappedCommand = ByteUtils.bytesToHexString(cipurseCardHandler.transmit(commandNonLe).getBytes());
        Assertions.assertThat(actualUnwrappedCommand).isEqualTo(expectedUnwrapCommand);

        cipurseCardHandler.secureMessagingIndicator = CipurseCardHandler.SMI_ENC_ENC_LE_PRESENT;
        cipurseCardHandler.secureMessagingFlag = true;
        ByteArray commandWithLe = new ByteArray(ByteUtils.fromHexString("00 A4 04 00 08 5F 00 00 00 00 00 00 01 02"));
        expectedUnwrapCommand = "01 02 03 04 90 00";
        actualUnwrappedCommand = ByteUtils.bytesToHexString(cipurseCardHandler.transmit(commandWithLe).getBytes());
        Assertions.assertThat(actualUnwrappedCommand).isEqualTo(expectedUnwrapCommand);

    }

    @Test
    public void shouldWrapAndUnwrapMACCommand() throws CipurseException {

        ICommsChannel commsChannel = new CipurseSecureMessageCommChannel();
        IAes aes = new AES();
        ILogger logger = new Logger();
        CipurseCardHandler cipurseCardHandler = new CipurseCardHandler(commsChannel, aes, logger);
        CommandAPI commandAPI = CommandAPIFactory.getInstance().buildCommandAPI();
        commandAPI.setVersion(CommandAPI.Version.V3);
        byte[] key = ByteUtils.fromHexString("73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73");
        boolean actualResult = cipurseCardHandler.setupSecureChannel((byte) 1, new ByteArray(key));
        Assertions.assertThat(actualResult).isTrue();
        String expectedUnwrapCommand;
        ;
        String actualUnwrappedCommand;

        cipurseCardHandler.secureMessagingIndicator = CipurseCardHandler.SMI_MAC_MAC;
        cipurseCardHandler.secureMessagingFlag = true;
        ByteArray commandNonLe = new ByteArray(ByteUtils.fromHexString("00 A4 04 00 08 5F 00 00 00 00 00 00 01"));
        expectedUnwrapCommand = "01 02 03 04 90 00";
        actualUnwrappedCommand = ByteUtils.bytesToHexString(cipurseCardHandler.transmit(commandNonLe).getBytes());
        Assertions.assertThat(actualUnwrappedCommand).isEqualTo(expectedUnwrapCommand);

        cipurseCardHandler.secureMessagingIndicator = CipurseCardHandler.SMI_MAC_MAC;
        cipurseCardHandler.secureMessagingFlag = true;
        ByteArray commandWithLe = new ByteArray(ByteUtils.fromHexString("00 A4 04 00 08 5F 00 00 00 00 00 00 01 02"));
        expectedUnwrapCommand = "01 02 03 04 90 00";
        actualUnwrappedCommand = ByteUtils.bytesToHexString(cipurseCardHandler.transmit(commandWithLe).getBytes());
        Assertions.assertThat(actualUnwrappedCommand).isEqualTo(expectedUnwrapCommand);

    }

    @Test
    public void shouldWrapAndUnwrapPLAINCommand() throws CipurseException {

        ICommsChannel commsChannel = new CipurseSecureMessageCommChannel();
        IAes aes = new AES();
        ILogger logger = new Logger();
        CipurseCardHandler cipurseCardHandler = new CipurseCardHandler(commsChannel, aes, logger);
        CommandAPI commandAPI = CommandAPIFactory.getInstance().buildCommandAPI();
        commandAPI.setVersion(CommandAPI.Version.V3);
        byte[] key = ByteUtils.fromHexString("73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73");
        boolean actualResult = cipurseCardHandler.setupSecureChannel((byte) 1, new ByteArray(key));
        Assertions.assertThat(actualResult).isTrue();
        String expectedUnwrapCommand;
        ;
        String actualUnwrappedCommand;
        cipurseCardHandler.secureMessagingIndicator = CipurseCardHandler.SMI_PLAIN_PLAIN;
        cipurseCardHandler.secureMessagingFlag = true;
        ByteArray commandNonLe = new ByteArray(ByteUtils.fromHexString("00 A4 04 00 08 5F 00 00 00 00 00 00 01"));
        expectedUnwrapCommand = "01 02 03 04 90 00";
        actualUnwrappedCommand = ByteUtils.bytesToHexString(cipurseCardHandler.transmit(commandNonLe).getBytes());
        Assertions.assertThat(actualUnwrappedCommand).isEqualTo(expectedUnwrapCommand);

        cipurseCardHandler.secureMessagingIndicator = CipurseCardHandler.SMI_PLAIN_PLAIN;
        cipurseCardHandler.secureMessagingFlag = true;
        ByteArray commandWithLe = new ByteArray(ByteUtils.fromHexString("00 A4 04 00 08 5F 00 00 00 00 00 00 01 02"));
        expectedUnwrapCommand = "01 02 03 04 90 00";
        actualUnwrappedCommand = ByteUtils.bytesToHexString(cipurseCardHandler.transmit(commandWithLe).getBytes());
        Assertions.assertThat(actualUnwrappedCommand).isEqualTo(expectedUnwrapCommand);

    }

    @Test
    public void shouldWrapENCedAndUnwrapMACCommand() throws CipurseException {

        ICommsChannel commsChannel = new CipurseSecureMessageCommChannel();
        IAes aes = new AES();
        ILogger logger = new Logger();
        CipurseCardHandler cipurseCardHandler = new CipurseCardHandler(commsChannel, aes, logger);
        CommandAPI commandAPI = CommandAPIFactory.getInstance().buildCommandAPI();
        commandAPI.setVersion(CommandAPI.Version.V3);
        byte[] key = ByteUtils.fromHexString("73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73");
        boolean actualResult = cipurseCardHandler.setupSecureChannel((byte) 1, new ByteArray(key));
        Assertions.assertThat(actualResult).isTrue();
        String expectedUnwrapCommand;
        ;
        String actualUnwrappedCommand;

        cipurseCardHandler.secureMessagingIndicator = CipurseCardHandler.SMI_ENC_MAC;
        cipurseCardHandler.secureMessagingFlag = true;
        ByteArray commandNonLe = new ByteArray(ByteUtils.fromHexString("00 A4 04 00 08 5F 00 00 00 00 00 00 01"));
        expectedUnwrapCommand = "01 02 03 04 90 00";
        actualUnwrappedCommand = ByteUtils.bytesToHexString(cipurseCardHandler.transmit(commandNonLe).getBytes());
        Assertions.assertThat(actualUnwrappedCommand).isEqualTo(expectedUnwrapCommand);

        cipurseCardHandler.secureMessagingIndicator = CipurseCardHandler.SMI_ENC_MAC_LE_PRESENT;
        cipurseCardHandler.secureMessagingFlag = true;
        ByteArray commandWithLe = new ByteArray(ByteUtils.fromHexString("00 A4 04 00 08 5F 00 00 00 00 00 00 01 02"));
        expectedUnwrapCommand = "01 02 03 04 90 00";
        actualUnwrappedCommand = ByteUtils.bytesToHexString(cipurseCardHandler.transmit(commandWithLe).getBytes());
        Assertions.assertThat(actualUnwrappedCommand).isEqualTo(expectedUnwrapCommand);

    }

    @Test
    public void shouldCalculateKVV() throws CipurseException {
        CipurseSecureMessage cipurseSecureMessage = CipurseSecureMessage.getInstance(new AES(), new Logger());
        byte[] key = ByteUtils.fromHexString("73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73");
        String expectedKVV = "5F D6 7B";
        Assertions.assertThat(ByteUtils.bytesToHexString(cipurseSecureMessage.getKVV(key))).isEqualTo(expectedKVV);
    }
}