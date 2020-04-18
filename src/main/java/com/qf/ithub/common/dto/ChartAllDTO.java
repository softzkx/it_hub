package com.qf.ithub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/4/11 11:34
 * FileName: ChartAllDTO
 * Description: ${DESCRIPTION}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChartAllDTO {

    // 种类id
    private Integer id;
    // 种类名称
    private String name;
    // 个数
    private Integer count;
}
