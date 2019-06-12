package com.lance.spring.v1.annotation;

import java.lang.annotation.*;

/**
 * Date: 2019/6/7
 * Author: Lance
 * Class action:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
}
