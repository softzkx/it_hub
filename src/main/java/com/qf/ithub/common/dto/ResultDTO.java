package com.qf.ithub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/10 8:58
 * FileName: ResonseBoy
 * Description: ${DESCRIPTION}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDTO<T>{

    private Integer status;
    private String message;
    private String returnCode;
    private T data;
}
