package com.github.duychuongvn.cirpusecard.core.constant;

import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;

/**
 * Created by huynhduychuong on 4/27/2016.
 */
public enum ApduCase {

    CASE1(false, "As in short length, this case is not affected. "),
    CASE2(false, "The legacy case 2 from previous Java Card technology releases. LE has a value of 1 to 255"),
    CASE2_EXTENDED(true, "The extended version of case 2S, where LE is greater than 255"),
    CASE3(false, "The legacy case 3 case. LC is less than 256 bytes of data, and LE is zero."),
    CASE3_EXTENDED(true, "The extended version of Case 3, where LC is greater than 255, and LE is zero"),
    CASE4(false, "The legacy case 4. LC and LE are less than 256 bytes of data."),
    CASE4_EXTENDED(true, "The extended version of Case 4. LC or LE are greater than 256 bytes of data. "),;
    private boolean extended;
    private String description;

    ApduCase(boolean isExtended, String description) {
        this.extended = isExtended;
        this.description = description;
    }

    public boolean isExtended() {
        return extended;
    }

    public String getDescription() {
        return description;
    }

    public static ApduCase getCase(byte[] command) {
        if (command == null || command.length < 4) {

            throw new IllegalArgumentException("Command length must be greater than 4");
        } else if (command.length == 4) { // {CLA-INS-P1-P2}
            return CASE1;
        } else if (command.length == 5) { // {CLA-INS-P1-P2-Le}
            return CASE2;
        } else if (command.length == 7 && command[4] == 0) { // // {CLA-INS-P1-P2-Le} where Ne=3
            return CASE2_EXTENDED;
        }
        byte offset;
        if (command[4] == 0) {
            short lc1 = ByteUtils.getShort(command, (short) 5);
            offset = 7;
            if (lc1 + offset == command.length) {
                return CASE3_EXTENDED;
            } else if (lc1 + offset + 2 == command.length) {
                return CASE4_EXTENDED;
            } else {
                throw new IllegalArgumentException("Invalid extended C-APDU: Lc or Le is invalid");
            }
        } else {
            int lc = command[4] & 255;
            offset = 5;
            if (lc + offset == command.length) {
                return CASE3;
            } else if (lc + offset + 1 == command.length) {
                return CASE4;
            } else {
                throw new IllegalArgumentException("Invalid C-APDU: Lc or Le is invalid");
            }
        }
    }
}
