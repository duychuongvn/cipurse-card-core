package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.util.CommandApdu;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface ElementFile extends CipurseFile {

    byte[] read(CommandApdu commandApdu);
    byte[] update(CommandApdu commandApdu);
}
