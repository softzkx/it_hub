package com.qf.ithub.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/10 7:42
 * FileName: AppException
 * Description: ${DESCRIPTION}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppException extends RuntimeException{
    private Integer status;
    private String returnCode;
    private String message;
}
