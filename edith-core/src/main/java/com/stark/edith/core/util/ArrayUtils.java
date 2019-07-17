package com.stark.edith.core.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author fuyongde
 * @date 2019/7/17
 */
public class ArrayUtils {

    public static int getLength(Object array) {
        return Objects.isNull(array) ? 0 : Array.getLength(array);
    }

    public static int getLength(Object[] array) {
        return Objects.isNull(array) ? 0 : Array.getLength(array);
    }

    public static boolean isEmpty(Object[] array) {
        return getLength(array) == 0;
    }

    public static boolean isEmpty(int[] array) {
        return getLength(array) == 0;
    }

    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(int[] array) {
        return !isEmpty(array);
    }

    public static <T> List<T> toList(T[] array) {
        return Arrays.asList(array);
    }
}
