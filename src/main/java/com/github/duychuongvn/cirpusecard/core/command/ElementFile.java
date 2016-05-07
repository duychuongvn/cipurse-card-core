package com.github.duychuongvn.cirpusecard.core.command;

import org.osptalliance.cipurse.commands.EFFileAttributes;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface ElementFile extends CipurseFile {
    EFFileAttributes getEfFileAttributes();
    byte[] execute(CommandApdu commandApdu);
//    byte[] read(CommandApdu commandApdu);
//    byte[] update(CommandApdu commandApdu);
}
