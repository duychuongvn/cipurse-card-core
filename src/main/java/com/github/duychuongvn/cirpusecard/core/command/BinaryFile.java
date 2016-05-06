package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.util.CommandApdu;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface BinaryFile extends CipurseFile {
    byte[] readBinary(CommandApdu commandApdu);
    byte[] updateBinary(CommandApdu commandApdu);
}
