package com.lance.spring.v1.annotation;

import javax.annotation.Resource;
import java.lang.annotation.*;

/**
 * Date: 2019/6/7
 * Author: Lance
 * Class action:
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowried {
    String value() default "";
}
