package com.github.duychuongvn.cirpusecard.core.iso7816;

import java.util.Arrays;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public class TagAndLength {
    private Tag tag;
    private int length;

    public TagAndLength(final Tag tag, final int length) {
        this.tag = tag;
        this.length = length;
    }

    public Tag getTag() {
        return tag;
    }

    public int getLength() {
        return length;
    }

    public byte[] getBytes() {
        byte[] tagBytes = tag.getTagBytes();
        byte[] tagAndLengthBytes = Arrays.copyOf(tagBytes, tagBytes.length + 1);
        tagAndLengthBytes[tagAndLengthBytes.length - 1] = (byte) length;
        return tagAndLengthBytes;
    }

    @Override
    public String toString() {
        return tag.toString() + " length: " + length;
    }

}
