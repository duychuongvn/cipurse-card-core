package com.github.duychuongvn.cirpusecard.core.command.impl;

import com.github.duychuongvn.cirpusecard.core.command.ADFFile;
import com.github.duychuongvn.cirpusecard.core.command.CipurseFile;
import com.github.duychuongvn.cirpusecard.core.command.CommandApdu;
import com.github.duychuongvn.cirpusecard.core.command.ElementFile;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public abstract  class ElementFileImpl implements ElementFile {

    protected boolean activated;
    protected ADFFile currentADF;
    public ElementFileImpl(ADFFile currentADF) {
        this.currentADF = currentADF;
    }
    public abstract byte[] readFileAttributes(CommandApdu commandApdu);

    public abstract byte[] updateFileAttributes(CommandApdu commandApdu);
}
