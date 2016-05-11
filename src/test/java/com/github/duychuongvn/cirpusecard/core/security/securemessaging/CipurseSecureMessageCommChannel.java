package com.github.duychuongvn.cirpusecard.core.security.securemessaging;

import com.github.duychuongvn.cirpusecard.core.command.CommandApdu;
import com.github.duychuongvn.cirpusecard.core.constant.SwEnum;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.osptalliance.cipurse.CipurseException;
import org.osptalliance.cipurse.ICommsChannel;

/**
 * Created by huynhduychuong on 5/11/2016.
 */
public class CipurseSecureMessageCommChannel implements ICommsChannel {

    private final CipurseSecureMessage cipurseSecureMessage;

    public CipurseSecureMessageCommChannel() {
        cipurseSecureMessage = new CipurseSecureMessage(new AES(), new Logger());
        byte[] key = ByteUtils.fromHexString("73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73");
        cipurseSecureMessage.setSelectedKey(key);
        cipurseSecureMessage.setSelectedKey(key);
    }

    public byte[] transmit(byte[] bytes) throws CipurseException {
        String commmand = ByteUtils.bytesToHexString(bytes);
        try {
            byte[] response = new byte[0];
            if ("00 84 00 00 16".equals(commmand)) {
                response = cipurseSecureMessage.buildGetChallenge(new CommandApdu(bytes));
            } else if (commmand.startsWith("00 82 00 01")) {
                response = cipurseSecureMessage.finishMutualAuthenticate(bytes);
            }
            byte[] resultWithStatus = new byte[response.length + 2];
            System.arraycopy(response, 0, resultWithStatus, 0, response.length);
            System.arraycopy(new byte[]{(byte) 0x90, (byte) 0x00}, 0, resultWithStatus, response.length, 2);
            return resultWithStatus;
        }catch (Exception ex) {
            return SwEnum.SW_UNKNOWN.toBytes();
        }
    }

    public void open() throws CipurseException {

    }

    public void close() throws CipurseException {

    }

    public byte[] reset(int i) throws CipurseException {
        return new byte[0];
    }

    public boolean isOpen() throws CipurseException {
        return false;
    }
}
