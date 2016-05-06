package com.github.duychuongvn.cirpusecard.core.iso7816;

import com.github.duychuongvn.cirpusecard.core.constant.TagValueTypeEnum;
import com.github.duychuongvn.cirpusecard.core.iso7816.impl.TagImpl;
import org.mockito.internal.util.reflection.Fields;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public class CipurseTag {
    private static LinkedHashMap<ByteArrayWrapper, Tag> tags = new LinkedHashMap<ByteArrayWrapper, Tag>();
    private static final Tag FCP_TEMPLATE = new TagImpl("62", TagValueTypeEnum.BINARY, "FCP Template", "FCP Template of CIPURSE ADF");
    private static final Tag SECURITY_ATTRIBUTE = new TagImpl("86", TagValueTypeEnum.BINARY, "Security Attribute Information", "Security Attribute Information in proprietary format");
    private static final Tag ADF_APPLICATION_IDENTIFIER = new TagImpl("84", TagValueTypeEnum.BINARY, "Application Identifier of ADF", "Application Identifier of ADF");
    private static final Tag INDICATOR = new TagImpl("D4", TagValueTypeEnum.BINARY, "Indicator", "AID of PxSE ADF this application wants to register to");

    static {
        Field[] fields = CipurseTag.class.getFields();
        for (Field field : fields) {
            if (field.getType() == Tag.class) {
                try {
                    Tag tag = (Tag) field.get(null);
                    addTag(tag);
                } catch (IllegalAccessException e) {
                    // not error
                }
            }
        }
    }

    private static void addTag(Tag tag) {
        ByteArrayWrapper baw = ByteArrayWrapper.wrapperAround(tag.getTagBytes());
        if (tags.containsKey(baw)) {
            throw new IllegalArgumentException("Tag already added " + tag);
        }
        tags.put(baw, tag);
    }

    public static Tag getNotNull(final byte[] tagBytes) {
        Tag tag = find(tagBytes);
        if (tag == null) {
            tag = createUnknownTag(tagBytes);
        }
        return tag;
    }

    public static Tag createUnknownTag(final byte[] tagBytes) {
        return new TagImpl(tagBytes, TagValueTypeEnum.BINARY, "[UNKNOWN TAG]", "");
    }

    /**
     * Returns null if Tag not found
     */
    public static Tag find(final byte[] tagBytes) {
        return tags.get(ByteArrayWrapper.wrapperAround(tagBytes));
    }

    private CipurseTag() throws IllegalAccessException {
        throw new IllegalAccessException("This constructor not allowed");
    }
}
