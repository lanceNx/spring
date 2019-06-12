package com.lance.spring.v1.annotation;

import java.lang.annotation.*;

/**
 * Date: 2019/6/7
 * Author: Lance
 * Class action:
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
