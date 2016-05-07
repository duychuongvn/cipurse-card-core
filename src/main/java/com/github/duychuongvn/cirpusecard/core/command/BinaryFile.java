package com.github.duychuongvn.cirpusecard.core.command;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface BinaryFile extends ElementFile {
    byte[] readBinary(CommandApdu commandApdu);
    byte[] updateBinary(CommandApdu commandApdu);
}
