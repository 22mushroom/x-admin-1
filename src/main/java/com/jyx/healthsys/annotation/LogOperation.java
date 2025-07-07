package com.jyx.healthsys.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    String value();  // 操作描述，比如“登录”、“新增用户”
}