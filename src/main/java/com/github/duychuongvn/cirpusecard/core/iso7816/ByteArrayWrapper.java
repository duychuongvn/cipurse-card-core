package com.github.duychuongvn.cirpusecard.core.iso7816;

import java.util.Arrays;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public class ByteArrayWrapper {
    private final byte[] data;
    private final int hashcode;

    private ByteArrayWrapper(final byte[] data) {
        this.data = data;
        hashcode = Arrays.hashCode(data);
    }

    public static ByteArrayWrapper wrapperAround(final byte[] data) {
        if (data == null) {
            throw new NullPointerException();
        }
        return new ByteArrayWrapper(data);
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ByteArrayWrapper)) {
            return false;
        }
        return Arrays.equals(data, ((ByteArrayWrapper) other).data);
    }

    @Override
    public int hashCode() {
        return hashcode;
    }
}
