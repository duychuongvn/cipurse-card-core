package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.constant.ApduCase;
import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.constant.SwEnum;
import com.github.duychuongvn.cirpusecard.core.exception.EnumParserException;
import com.github.duychuongvn.cirpusecard.core.exception.Iso7816Exception;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import com.github.duychuongvn.cirpusecard.core.util.EnumUtil;

import java.io.ByteArrayInputStream;

/**
 * Created by huynhduychuong on 4/26/2016.
 */
public class CommandApdu {
    private int cla = 0x00;
    private int ins = 0x00;
    private int p1 = 0x00;
    private int p2 = 0x00;
    private int lc = 0x00;
    private int le = 0x00;
    private boolean leUsed = false;
    private byte[] data = new byte[0];
    private CommandEnum commandEnum;

    public CommandApdu(final CommandEnum commandEnum, int p1, int p2, byte[] data) {
        this.cla = commandEnum.getCla();
        this.ins = commandEnum.getIns();
        this.p1 = p1;
        this.p2 = p2;
        this.commandEnum = commandEnum;
        boolean hasData = data != null && data.length > 0;
        if (hasData) {
            this.lc = data.length;
            this.data = data.clone();
        }

    }

    public CommandApdu(CommandEnum commandEnum, int p1, int p2, byte[] data, int le) {
        this.cla = commandEnum.getCla();
        this.ins = commandEnum.getIns();
        this.p1 = p1;
        this.p2 = p2;
        this.commandEnum = commandEnum;
        boolean hasData = data != null && data.length > 0;
        if (hasData) {
            this.lc = data.length;
            this.data = data.clone();
        }
        this.le = le;
        this.leUsed = true;

    }

    public CommandApdu(byte[] command) {

        int commandId = ByteUtils.byteArrayToInt(new byte[]{command[0], command[1]});
        try {
            this.commandEnum = EnumUtil.getEnum(commandId, CommandEnum.class);
        }catch (EnumParserException exception) {
            throw  new Iso7816Exception(SwEnum.SW_INS_NOT_SUPPORTED);
        }
        this.p1 = command[2];
        this.p2 = command[3];
        this.cla = commandEnum.getCla();
        this.ins = commandEnum.getIns();
        ApduCase apduCase = ApduCase.getCase(command);
        switch (apduCase) {
            case CASE1:
                break;
            case CASE2:
                this.le =  ByteUtils.byteToInt(command[4]) ;
                if (le == 0) {
                    le = 256;
                }
                leUsed = true;
                break;
            case CASE2_EXTENDED:
                le = ByteUtils.byteArrayToInt(new byte[]{command[5], command[6]});
                leUsed = true;
                break;
            case CASE3:
                lc =  ByteUtils.byteToInt(command[4]);
                data = new byte[lc];
                System.arraycopy(command, 5, data, 0, lc);
                break;
            case CASE3_EXTENDED:
                lc = ByteUtils.byteArrayToInt(new byte[]{command[5], command[6]});
                data = new byte[lc];
                System.arraycopy(command, 7, data, 0, lc);
                break;
            case CASE4:
                lc =  ByteUtils.byteToInt(command[4]);
                le =  ByteUtils.byteToInt(command[command.length - 1]);
                data = new byte[lc];
                System.arraycopy(command, 5, data, 0, lc);
                leUsed = true;
                break;
            case CASE4_EXTENDED:
                lc = ByteUtils.byteArrayToInt(new byte[]{command[5], command[6]});
                data = new byte[lc];
                System.arraycopy(command, 7, data, 0, lc);
                le = ByteUtils.getShort(command, (short) (command.length - 2));
                leUsed = true;
                break;
            default:
                throw new Iso7816Exception(SwEnum.SW_DATA_INVALID);
        }


    }

    public CommandEnum getCommandEnum() {
        return commandEnum;
    }

    public int getCla() {
        return cla;
    }

    public int getIns() {
        return ins;
    }

    public int getP1() {
        return p1;
    }

    public int getP2() {
        return p2;
    }

    public int getLc() {
        return lc;
    }

    public int getLe() {
        return le;
    }

    public boolean isLeUsed() {
        return leUsed;
    }

    public byte[] getData() {
        return data;
    }

    public byte[] toBytes() {
        int length = 4; // CLA - INS - P1 - P2
        boolean isLcPresent = data.length > 0;
        if (isLcPresent) {
            if (lc < 256) {
                length += 1; // add Lc
            } else {
                length += 3;
            }
            length += data.length;
        }
        if (isLeUsed()) {
            if (le <= 256) {
                length += 1; // Le is 1 byte
            } else {
                if (isLcPresent) {
                    length += 2; // le is 2 bytes
                } else {
                    length += 3; // le is 3 bytes: 00 xx xx
                }
            }
        }
        byte[] command = new byte[length];
        int index = 0;
        command[index++] = (byte) cla;
        command[index++] = (byte) ins;
        command[index++] = (byte) p1;
        command[index++] = (byte) p2;
        if (isLcPresent) {

            if (lc < 256) {
                command[index++] = (byte) lc;
            } else {
                command[index++] = 0;
                byte[] lcBytes = ByteUtils.toByteArray(lc);
                command[index++] = lcBytes[2];
                command[index++] = lcBytes[3];
            }

        }
        System.arraycopy(data, 0, command, index, data.length);
        if (isLeUsed()) {
            if (le <= 256) {
                command[command.length - 1] = (byte) le;
            } else {
                byte[] leBytes = ByteUtils.toByteArray(le);
                command[command.length - 2] = leBytes[2];
                command[command.length - 1] = leBytes[3];
            }

        }
        return command;
    }
}
