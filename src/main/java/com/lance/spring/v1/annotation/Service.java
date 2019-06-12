package com.lance.spring.v1.annotation;

import java.lang.annotation.*;

/**
 * Date: 2019/6/7
 * Author: Lance
 * Class action:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
