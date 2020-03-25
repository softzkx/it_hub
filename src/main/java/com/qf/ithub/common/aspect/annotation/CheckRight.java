package com.qf.ithub.common.aspect.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/10 20:58
 * FileName: CheckRight
 * Description: ${DESCRIPTION}
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRight {

    String value();
}
