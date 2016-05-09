package com.github.duychuongvn.cirpusecard.core.util;

/**
 * Created by huynhduychuong on 5/9/2016.
 */
public class ArrayUtils {
    public static boolean contains(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind) != -1;
    }
    public static int indexOf(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }
    public static int indexOf(long[] array, long valueToFind) {
        return indexOf(array, valueToFind, 0);
    }
    public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            int i;
            if(objectToFind == null) {
                for(i = startIndex; i < array.length; ++i) {
                    if(array[i] == null) {
                        return i;
                    }
                }
            } else if(array.getClass().getComponentType().isInstance(objectToFind)) {
                for(i = startIndex; i < array.length; ++i) {
                    if(objectToFind.equals(array[i])) {
                        return i;
                    }
                }
            }

            return -1;
        }
    }

    public static int indexOf(long[] array, long valueToFind, int startIndex) {
        if(array == null) {
            return -1;
        } else {
            if(startIndex < 0) {
                startIndex = 0;
            }

            for(int i = startIndex; i < array.length; ++i) {
                if(valueToFind == array[i]) {
                    return i;
                }
            }

            return -1;
        }
    }
}
