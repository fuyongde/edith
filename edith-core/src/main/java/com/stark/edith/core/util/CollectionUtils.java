package com.stark.edith.core.util;

import java.util.Collection;

/**
 * @author fuyongde
 * @date 2019/7/15
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }
}
