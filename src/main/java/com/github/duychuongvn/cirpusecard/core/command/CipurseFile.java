package com.github.duychuongvn.cirpusecard.core.command;

import org.osptalliance.cipurse.commands.EFFileAttributes;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface CipurseFile {
    byte[] createFile(CommandApdu commandApdu);
    byte[] readFileAttributes(CommandApdu commandApdu);
    byte[] updateFileAttributes(CommandApdu commandApdu);

}
