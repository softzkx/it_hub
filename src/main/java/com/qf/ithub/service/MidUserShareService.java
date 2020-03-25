package com.qf.ithub.service;

import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.entity.MidUserShare;
import com.qf.ithub.mapper.MidUserShareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/25 12:16
 * FileName: MidUserShareService
 * Description: ${DESCRIPTION}
 */
@Service
public class MidUserShareService {

    @Resource
    private MidUserShareMapper midUserShareMapper;

    public ResultDTO getMidUserShare(Integer userid, Integer shareid) {
        Example example = new Example(MidUserShare.class);
        example.createCriteria().andEqualTo("userId",userid)
                .andEqualTo("shareId",shareid);
        MidUserShare midUserShare = midUserShareMapper.selectOneByExample(example);
        return ResultDTO.builder()
                .status(HttpStatus.OK.value())
                .data(midUserShare!=null)
                .build();

    }
}
