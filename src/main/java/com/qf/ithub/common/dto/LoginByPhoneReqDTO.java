package com.qf.ithub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/9 18:04
 * FileName: LoginDTO
 * Description: ${DESCRIPTION}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginByPhoneReqDTO {
    String phonenum;
    String checkcode;
}
