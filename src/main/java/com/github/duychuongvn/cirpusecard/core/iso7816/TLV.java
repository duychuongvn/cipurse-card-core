package com.github.duychuongvn.cirpusecard.core.iso7816;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public class TLV {

    private final Tag tag;
    private final int length;
    private final byte[] value;
    private final byte[] rawEncodedLenthBytes;


    public TLV(Tag tag, int length, byte[] rawEncodedLenthBytes, byte[] valueBytes) {
        if (valueBytes == null || length != valueBytes.length) {
            // Assert
            throw new IllegalArgumentException("length != bytes.length");
        }
        this.tag = tag;
        this.length = length;
        this.rawEncodedLenthBytes = rawEncodedLenthBytes.clone();
        this.value = valueBytes.clone();
    }

    public Tag getTag() {
        return tag;
    }

    public int getLength() {
        return length;
    }

    public byte[] getValue() {
        return value;
    }

    public byte[] getRawEncodedLenthBytes() {
        return rawEncodedLenthBytes;
    }

    public byte[] getTagBytes() {
        return this.tag.getTagBytes();
    }
}
