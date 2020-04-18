package com.qf.ithub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/4/6 11:52
 * FileName: RightDTO
 * Description: ${DESCRIPTION}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RightDTO {
    private Integer rightid;
    private String righttext;
    private Integer righttype;
    private String righturl;
    private Integer parentid;
    private Boolean isMenu;
    private List<RightDTO> children;
}
