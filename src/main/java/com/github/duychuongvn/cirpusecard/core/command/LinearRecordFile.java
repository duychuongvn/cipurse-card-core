package com.github.duychuongvn.cirpusecard.core.command;

import com.github.duychuongvn.cirpusecard.core.util.CommandApdu;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface LinearRecordFile extends CipurseFile {
    byte[] readRecord(CommandApdu commandApdu);

    byte[] updateRecord(CommandApdu commandApdu);
}
