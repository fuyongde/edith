package com.stark.edith.core.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author fuyongde
 * @date 2019/7/17
 */
public class ArrayUtilsTest {

    @Test
    public void getLength() {
        int[] intArray = new int[]{1, 2, 3};
        assertEquals(3, ArrayUtils.getLength(intArray));

        String[] stringArray = new String[]{"a", "b", "c"};
        assertEquals(3, ArrayUtils.getLength(stringArray));
    }

    @Test
    public void isEmpty() {
        assertTrue(ArrayUtils.isEmpty((Object[]) null));
        assertTrue(ArrayUtils.isEmpty(new int[]{}));
        assertTrue(ArrayUtils.isEmpty(new String[]{}));
    }

    @Test
    public void isNotEmpty() {
        int[] intArray = new int[]{1, 2, 3};
        assertTrue(ArrayUtils.isNotEmpty(intArray));

        String[] stringArray = new String[]{"a", "b", "c"};
        assertTrue(ArrayUtils.isNotEmpty(stringArray));
    }

    @Test
    public void toList() {
        String[] stringArray = new String[]{"a", "b", "c"};
        List<String> stringList = ArrayUtils.toList(stringArray);
    }
}