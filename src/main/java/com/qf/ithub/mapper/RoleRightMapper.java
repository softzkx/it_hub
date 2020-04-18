package com.qf.ithub.mapper;

import com.qf.ithub.entity.RoleRight;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleRightMapper extends Mapper<RoleRight> {

    public List<Integer> getRightidsByRoleidLeave(@Param("roleid") Integer roleid);
}