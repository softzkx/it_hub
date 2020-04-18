package com.qf.ithub.controller;

import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.entity.Role;
import com.qf.ithub.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/4/7 8:50
 * FileName: RolesController
 * Description: ${DESCRIPTION}
 */
@RestController
@RequestMapping("/roles")
@CrossOrigin(origins="*",maxAge = 3600)
public class RolesController {

    @Autowired
    private RolesService rolesService;

    @GetMapping("/all")
    public ResultDTO getAllRoles(){

        return rolesService.getAllRoles();
    }

    /**
     * 添加角色
     * @param roleName
     * @return
     */
    @PostMapping("/addrole")
    public ResultDTO addRole(String roleName){
        System.out.println(roleName);
        return rolesService.addRole(roleName);
    }
}
