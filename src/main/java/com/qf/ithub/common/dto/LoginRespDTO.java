package com.qf.ithub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/17 19:41
 * FileName: LoginRespDTO
 * Description: ${DESCRIPTION}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRespDTO {

    private String token;
    private Integer id;
    private Integer bonus;
    private Boolean isvip;
}
