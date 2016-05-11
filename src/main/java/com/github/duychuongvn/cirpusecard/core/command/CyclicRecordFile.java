package com.github.duychuongvn.cirpusecard.core.command;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface CyclicRecordFile extends ElementFile {
    byte[] appendRecord(CommandApdu commandApdu);

    byte[] updateRecord(CommandApdu commandApdu);

    byte[] readRecord(CommandApdu commandApdu);
}
