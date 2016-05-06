package com.github.duychuongvn.cirpusecard.core.iso7816.impl;

import com.github.duychuongvn.cirpusecard.core.constant.TagValueTypeEnum;
import com.github.duychuongvn.cirpusecard.core.iso7816.Tag;
import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public class TagImplTest {
    @Test
    public void testMethod() {

        TagImpl tag = new TagImpl("90", TagValueTypeEnum.MIXED, "", "description test");
        TagImpl tag2 = new TagImpl("9000", TagValueTypeEnum.MIXED, "", "description test");

        Assertions.assertThat(tag.toString()).isEqualTo(
                "Tag[90] Name=, TagType=PRIMITIVE, ValueType=MIXED, Class=CONTEXT_SPECIFIC");
        Assertions.assertThat(tag.getTagClass()).isEqualTo(Tag.Class.CONTEXT_SPECIFIC);

        // Equals test
        Assertions.assertThat(tag.equals(null)).isFalse();
        Assertions.assertThat(tag.equals(tag2)).isFalse();
        Assertions.assertThat(tag.equals(tag)).isTrue();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {
        new TagImpl((byte[]) null, TagValueTypeEnum.MIXED, "", "description test");

    }

}
