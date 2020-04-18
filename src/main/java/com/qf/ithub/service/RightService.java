package com.qf.ithub.service;

import com.qf.ithub.common.dto.MenuGrantDTO;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.dto.RightDTO;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.entity.RoleRight;
import com.qf.ithub.entity.Rright;
import com.qf.ithub.mapper.RoleRightMapper;
import com.qf.ithub.mapper.RrightMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/4/6 12:03
 * FileName: RightService
 * Description: ${DESCRIPTION}
 */
@Service
public class RightService {

    @Resource
    private RrightMapper rrightMapper;
    @Resource
    private RoleRightMapper roleRightMapper;

    /**
     *  获得指定角色根目录往下所有的菜单权限节点
     */
    public List<RightDTO> getMenusByRoleid(Integer roleid){
        List<RightDTO> roots = rrightMapper.getMenuRightsByRoleidAndParatentid(roleid, 0);
        if (roots != null && roots.size()>0){
            for (RightDTO root : roots) {
                // 获得id  再往下查
                Integer pid = root.getRightid();
                List<RightDTO> chridren = rrightMapper.getMenuRightsByRoleidAndParatentid(roleid, pid);
                if (chridren != null && chridren.size()>0) {
                    root.setChildren(chridren);
                }
            }
        }
        return roots;
    }


    /**
     *  获得指定角色的能够访问menu 权限的集合（更新角色用）
     */
    public ResultDTO getMenusByRoleidForGrant(Integer roleid) {

        // 1: 获得所有的parentid=0 的权限
        List<RightDTO> roots = rrightMapper.getAllRightsByParatentid( 0);
        // 获得下一集
        if (roots != null && roots.size()>0){
            for (RightDTO root : roots) {
                // 获得id  再往下查
                Integer pid = root.getRightid();
                List<RightDTO> chridren = rrightMapper.getAllRightsByParatentid( pid);
                if (chridren != null && chridren.size()>0) {
                    root.setChildren(chridren);
                }
            }
        }

        // 2 获得所有的该角色的拥有权限的集合
        List<Integer> keys = roleRightMapper.getRightidsByRoleidLeave(roleid);


        //3 返回
        MenuGrantDTO build = MenuGrantDTO.builder()
                .allRights(roots)
                .checkedRights(keys)
                .roleid(roleid).build();
        return ResultDTO.builder()
                .status(HttpStatus.OK.value())
                .data(build)
                .build();


    }

    /**
     *  给指定的角色赋权
     */
    public ResultDTO grant(MenuGrantDTO menuGrantDTO) {

        // 1 先删除指定的角色的权限
        Example example = new Example(RoleRight.class);
        example.createCriteria().andEqualTo("roleid",menuGrantDTO.getRoleid());
        int count = roleRightMapper.deleteByExample(example);
        // 2 在添加角色的权限
        for (Integer checkedRight : menuGrantDTO.getCheckedRights()) {
            RoleRight roleRight = RoleRight.builder()
                    .rightid(checkedRight)
                    .roleid(menuGrantDTO.getRoleid())
                    .build();
            count = roleRightMapper.insertSelective(roleRight);
            if(count!=1) {
                throw AppException.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("添加角色的权限失败")
                        .build();
            }
        }
        return ResultDTO.builder().status(200)
                .message("修改角色的权限成功")
                .build();
    }
}
