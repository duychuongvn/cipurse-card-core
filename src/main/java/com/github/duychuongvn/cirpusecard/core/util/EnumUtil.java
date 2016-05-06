package com.github.duychuongvn.cirpusecard.core.util;

import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.constant.EnumKey;
import com.github.duychuongvn.cirpusecard.core.exception.EnumParserException;

/**
 * Created by huynhduychuong on 4/27/2016.
 */
public class EnumUtil {
    public static <T extends EnumKey> T getEnum(int key, Class<T> className) {
        T[] enums = className.getEnumConstants();
        for (T entity : enums) {
            if (entity.getKey() == key) {
                return entity;
            }
        }
        throw new EnumParserException(key, className.getSimpleName());
    }
}
