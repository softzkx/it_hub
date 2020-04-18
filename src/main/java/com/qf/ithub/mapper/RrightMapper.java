package com.qf.ithub.mapper;

import com.qf.ithub.common.dto.RightDTO;
import com.qf.ithub.entity.Rright;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RrightMapper extends Mapper<Rright> {

    public List<RightDTO> getMenuRightsByRoleidAndParatentid(@Param("roleid") Integer roleid, @Param("parentid") Integer parentid);

    public List<RightDTO> getAllRightsByParatentid(@Param("parentid") Integer parentid);
}