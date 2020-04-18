package com.qf.ithub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/4/7 16:10
 * FileName: MenuGrantDTO
 * Description: ${DESCRIPTION}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuGrantDTO {
    // 1 所有的权限
   private List<RightDTO> allRights;
    // 2 拥有的权限的集合
    private List<Integer> checkedRights;
    // 3 角色id
    private Integer roleid;
}
