package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.util.CommandApdu;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface ADFFile extends CipurseFile {
    byte[] deactiveFile(CommandApdu commandApdu);
    byte[] activeFile(CommandApdu commandApdu);
    byte[] updateKey(CommandApdu commandApdu);
    byte[] updateKeyAttributes(CommandApdu commandApdu);
    byte[] createEF(CommandApdu commandApdu);
}
