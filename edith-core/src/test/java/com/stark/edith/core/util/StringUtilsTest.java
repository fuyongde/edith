package com.stark.edith.core.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author fuyongde
 * @date 2019/7/15
 */
public class StringUtilsTest {

    @Test
    public void isNullOrEmpty() {
        assertTrue(StringUtils.isNullOrEmpty(null));
        assertTrue(StringUtils.isNullOrEmpty(""));
        assertFalse(StringUtils.isNullOrEmpty(" "));
        assertFalse(StringUtils.isNullOrEmpty("bob"));
        assertFalse(StringUtils.isNullOrEmpty(" bob "));
    }

    @Test
    public void isBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertFalse(StringUtils.isBlank("bob"));
        assertFalse(StringUtils.isBlank(" bob "));
    }

    @Test
    public void isNotBlank() {
        assertTrue(StringUtils.isNotBlank("bob"));
        assertTrue(StringUtils.isNotBlank(" bob "));
    }
}