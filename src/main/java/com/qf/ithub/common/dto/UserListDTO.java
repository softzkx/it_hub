package com.qf.ithub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/4/8 9:17
 * FileName: UserListDTO
 * Description: ${DESCRIPTION}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListDTO {

    private Integer id;

    private Long phone;

    private String wxId;

    private String wxNickname;

    private Integer roleid;

    private String roles;

    private String avatarUrl;

    private Date createTime;

    private Date updateTime;

    private Integer bonus;

    private Boolean isvip;

    private Boolean isEdit;
}
