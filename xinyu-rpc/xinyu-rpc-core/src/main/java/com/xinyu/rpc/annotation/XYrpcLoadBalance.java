package com.xinyu.rpc.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XYrpcLoadBalance {

    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * lb策略
     * @return
     */
    String strategy() default "random";
}
