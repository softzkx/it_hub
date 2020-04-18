package com.qf.ithub.service;

import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.entity.Role;
import com.qf.ithub.mapper.RoleMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/4/7 8:52
 * FileName: RolesService
 * Description: ${DESCRIPTION}
 */
@Service
public class RolesService {

    @Resource
    private RoleMapper roleMapper;

    /**
     *  获得所有的角色对象
     * @return
     */
    public ResultDTO getAllRoles() {

        List<Role> roles = roleMapper.selectAll();
        return ResultDTO.builder()
                .status(HttpStatus.OK.value())
                .data(roles)
                .build();
    }


    /**
     * 添加角色
     * @param roleName
     * @return
     */
    public ResultDTO addRole(String roleName) {
        Role build = Role.builder()
                .rolename(roleName).build();

        int count = roleMapper.insertSelective(build);
        if (count==0){
            throw AppException.builder()
                    .message("添加角色失败")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        return ResultDTO.builder().status(HttpStatus.OK.value()).build();
    }
}
