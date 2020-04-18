package com.qf.ithub.controller;

import com.qf.ithub.common.aspect.annotation.CheckLogin;
import com.qf.ithub.common.aspect.annotation.CheckRight;
import com.qf.ithub.common.dto.MenuGrantDTO;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.dto.RightDTO;
import com.qf.ithub.service.RightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/4/6 14:49
 * FileName: RightsController
 * Description: ${DESCRIPTION}
 */
@RestController
@RequestMapping("/rights")
@CrossOrigin(origins="*",maxAge = 3600)
public class RightsController {

    @Autowired
    private RightService rightService;

    /**
     *  根据角色获得当前用户能够访问到的权利的集合
     */
    @GetMapping("/menus")
    @CheckLogin
    public List<RightDTO> getMenusByRoleid(HttpServletRequest request) {
        Integer roleid = (Integer) request.getAttribute("roleid");
        return rightService.getMenusByRoleid(roleid);
    }

    /**
     *  获得指定角色的能够访问menu 权限的集合（更新角色用）
     */
    @GetMapping("/menusgrant")
    @CheckRight("admin")
    public ResultDTO getMenusByRoleidForGrant(HttpServletRequest request,Integer roleid) {
        // Integer roleid = (Integer) request.getAttribute("roleid");
        return rightService.getMenusByRoleidForGrant(roleid);
    }

    /**
     *  给指定的角色赋权
     */
    @PostMapping("/grant")
    @CheckRight("admin")
    public ResultDTO grant(@RequestBody MenuGrantDTO menuGrantDTO) {
        // Integer roleid = (Integer) request.getAttribute("roleid");
        System.out.println(menuGrantDTO);
        return rightService.grant(menuGrantDTO);
    }
}
