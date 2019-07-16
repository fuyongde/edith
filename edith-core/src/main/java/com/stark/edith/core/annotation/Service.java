package com.stark.edith.core.annotation;

import java.lang.annotation.*;

/**
 * @author fuyongde
 * @date 2019/7/16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Service {

    boolean export() default true;
}
