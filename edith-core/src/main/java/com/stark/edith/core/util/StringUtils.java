package com.stark.edith.core.util;

import com.google.common.base.Strings;

/**
 * @author fuyongde
 * @date 2019/7/15
 */
public class StringUtils {

    public static boolean isNullOrEmpty(String string) {
        return Strings.isNullOrEmpty(string);
    }

    public static String nullToEmpty(String string) {
        return Strings.nullToEmpty(string);
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }
}
