package com.github.duychuongvn.cirpusecard.core.command;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface LinearRecordFile extends ElementFile {
    byte[] readRecord(CommandApdu commandApdu);

    byte[] updateRecord(CommandApdu commandApdu);
}
