package com.github.duychuongvn.cirpusecard.core.security.securemessaging;

import com.github.duychuongvn.cirpusecard.core.command.CommandApdu;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.osptalliance.cipurse.CipurseException;
import org.osptalliance.cipurse.IAes;
import org.osptalliance.cipurse.ICommsChannel;
import org.osptalliance.cipurse.ILogger;
import org.osptalliance.cipurse.commands.*;

/**
 * Created by huynhduychuong on 5/11/2016.
 */
public class CipurseSecureMessageTest {

    @Test
    public void shouldWrapCommand() throws CipurseException {

        ICommsChannel commsChannel = new CipurseSecureMessageCommChannel();
        IAes aes = new AES();
        ILogger logger = new Logger();
        CipurseCardHandler cipurseCardHandler = new CipurseCardHandler(commsChannel, aes, logger);
        CommandAPI commandAPI = CommandAPIFactory.getInstance().buildCommandAPI();
        commandAPI.setVersion(CommandAPI.Version.V3);
        ICipurseAdministration cipurseAdministration = commandAPI.getCipurseAdministration(cipurseCardHandler);
        ICipurseOperational cipurseOperational = commandAPI.getCipurseOperational(cipurseCardHandler);
        byte[] key = ByteUtils.fromHexString("73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73");
        boolean actualResult = cipurseCardHandler.setupSecureChannel((byte)1, new ByteArray(key) );
        Assertions.assertThat(actualResult).isTrue();
//        String commmand = "00 A4 00 00 02 5F 01";
//        String expectedWrappedCommand = "04 A4 00 00 03 00 5F 01";
//        CipurseSecureMessage secureMessage = CipurseSecureMessage.getInstance(new AES(), new Logger());
//        secureMessage.setKeyValues(new byte[][]{key});
//        secureMessage.setSelectedKey(key);
//        String getChallengeCommand = "00 84 00 00 16";
//        byte[] getChallengeResponse= secureMessage.buildGetChallenge(new CommandApdu(ByteUtils.fromHexString(getChallengeCommand)));
//
//
//        byte[] buffer = new byte[22];
//        secureMessage.finishGetChallenge(buffer, (short)16);
//        byte[] mutualAuthenticationBufferRequest = ByteUtils.fromHexString("00 82 00 01 26 93 78 DB CF 21 74 1C 96 B9 99 38 D5 96 54 F1 D9 8F 9F 06 7D 41 8A 77 0A 53 D8 AF 50 6D 99 34 DE 15 3B E9 00 BC 8B 10");
//        secureMessage.finishMutualAuthenticate(mutualAuthenticationBufferRequest);
//        String expectedBuffer = "2C 33 2F 94 50 83 3A 13 DF C7 71 7D 9B 3D 68 6B";
//        byte[] mutualAuthenticationBuffer = new byte[16];
//        System.arraycopy(mutualAuthenticationBufferRequest, 0, mutualAuthenticationBuffer, 0, mutualAuthenticationBuffer.length);
//        Assertions.assertThat(mutualAuthenticationBuffer).isEqualTo(ByteUtils.fromHexString(expectedBuffer));



    }

}