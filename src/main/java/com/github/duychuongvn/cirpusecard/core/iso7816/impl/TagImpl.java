package com.github.duychuongvn.cirpusecard.core.iso7816.impl;

import com.github.duychuongvn.cirpusecard.core.constant.TagTypeEnum;
import com.github.duychuongvn.cirpusecard.core.constant.TagValueTypeEnum;
import com.github.duychuongvn.cirpusecard.core.iso7816.Tag;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;

import java.util.Arrays;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public class TagImpl implements Tag {
    private final byte[] idBytes;
    public final String name;
    private final String description;
    private final TagValueTypeEnum tagValueType;
    private final Class tagClass;
    private final TagTypeEnum type;

    public TagImpl(final String id, final TagValueTypeEnum tagValueType, final String name, final String description) {
        this(ByteUtils.fromHexString(id), tagValueType, name, description);
    }

    public TagImpl(final byte[] idBytes, final TagValueTypeEnum tagValueType, final String name, final String description) {
        if (idBytes == null) {
            throw new IllegalArgumentException("Param id cannot be null");
        }
        if (idBytes.length == 0) {
            throw new IllegalArgumentException("Param id cannot be empty");
        }
        if (tagValueType == null) {
            throw new IllegalArgumentException("Param tagValueType cannot be null");
        }
        this.idBytes = idBytes;
        this.name = name;
        this.description = description;
        this.tagValueType = tagValueType;

        if (ByteUtils.matchBitByBitIndex(this.idBytes[0], 5)) {
            type = TagTypeEnum.CONSTRUCTED;
        } else {
            type = TagTypeEnum.PRIMITIVE;
        }
        // Bits 8 and 7 of the first byte of the tag field indicate a class.
        // The value 00 indicates a data object of the universal class.
        // The value 01 indicates a data object of the application class.
        // The value 10 indicates a data object of the context-specific class.
        // The value 11 indicates a data object of the private class.
        byte classValue = (byte) (this.idBytes[0] >>> 6 & 0x03);
        switch (classValue) {
            case (byte) 0x01:
                tagClass = Class.APPLICATION;
                break;
            case (byte) 0x02:
                tagClass = Class.CONTEXT_SPECIFIC;
                break;
            case (byte) 0x03:
                tagClass = Class.PRIVATE;
                break;
            default:
                tagClass = Class.UNIVERSAL;
                break;
        }

    }

    public boolean isConstructed() {
        return type == TagTypeEnum.CONSTRUCTED;
    }

    public byte[] getTagBytes() {
        return idBytes;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public TagValueTypeEnum getTagValueType() {
        return tagValueType;
    }


    public TagTypeEnum getType() {
        return type;
    }

    public Class getTagClass() {
        return tagClass;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Tag)) {
            return false;
        }
        Tag that = (Tag) other;
        if (getTagBytes().length != that.getTagBytes().length) {
            return false;
        }

        return Arrays.equals(getTagBytes(), that.getTagBytes());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Arrays.hashCode(idBytes);
        return hash;
    }

    public int getNumTagBytes() {
        return idBytes.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tag[");
        sb.append(ByteUtils.bytesToHexString(getTagBytes()));
        sb.append("] Name=");
        sb.append(getName());
        sb.append(", TagType=");
        sb.append(getType());
        sb.append(", ValueType=");
        sb.append(getTagValueType());
        sb.append(", Class=");
        sb.append(tagClass);
        return sb.toString();
    }
}
