package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.util.CommandApdu;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface CipurseFile {
    byte[] createFile(CommandApdu commandApdu);
    byte[] readFileAttributes(CommandApdu commandApdu);
    byte[] updateFileAttributes(CommandApdu commandApdu);

}
