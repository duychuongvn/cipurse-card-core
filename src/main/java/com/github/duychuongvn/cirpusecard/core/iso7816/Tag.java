package com.github.duychuongvn.cirpusecard.core.iso7816;

import com.github.duychuongvn.cirpusecard.core.constant.TagTypeEnum;
import com.github.duychuongvn.cirpusecard.core.constant.TagValueTypeEnum;

/**
 * Created by huynhduychuong on 4/28/2016.
 */
public interface Tag {

    enum Class {UNIVERSAL, APPLICATION, CONTEXT_SPECIFIC, PRIVATE}

    ;

    boolean isConstructed();

    byte[] getTagBytes();

    String getName();

    String getDescription();

    TagTypeEnum getType();

    TagValueTypeEnum getTagValueType();

    Class getTagClass();

    int getNumTagBytes();
}
