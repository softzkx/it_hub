package com.qf.ithub.service;

import com.github.pagehelper.PageHelper;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.entity.Share;
import com.qf.ithub.entity.ShareImages;
import com.qf.ithub.mapper.ShareImagesMapper;
import com.qf.ithub.mapper.ShareMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/24 10:48
 * FileName: ShareService
 * Description: ${DESCRIPTION}
 */
@Service
public class ShareService {

    @Resource
    private ShareMapper shareMapper;
    @Resource
    private ShareImagesMapper shareImagesMapper;

    /**
     * 获得首页轮播的商品前5条
     * @return
     */
    public ResultDTO getLunbos() {
        PageHelper.startPage(1,5);
        Example example = new Example(Share.class);
        example.createCriteria().andEqualTo("auditStatus","PASS")
                .andEqualTo("isTop",true);
        example.setOrderByClause("update_time desc");
        List<Share> shares = shareMapper.selectByExample(example);
        return ResultDTO.builder()
                .data(shares)
                .status(HttpStatus.OK.value()).build();


    }

    /**
     * 获得首页推荐商品的前10条
     */
    public ResultDTO getHots() {
        PageHelper.startPage(1,10);
        Example example = new Example(Share.class);
        example.createCriteria().andEqualTo("auditStatus","PASS")
                .andEqualTo("isHot",true);
        example.setOrderByClause("update_time desc");
        List<Share> shares = shareMapper.selectByExample(example);
        return ResultDTO.builder()
                .data(shares)
                .status(HttpStatus.OK.value()).build();


    }

    /***
     * 获得详情页上面 某个分享的图片的集合
     */
    public ResultDTO getImagesById(Integer shareid) {
        Example example = new Example(ShareImages.class);
        example.createCriteria().andEqualTo("shareid",shareid);
        List<ShareImages> shareImages = shareImagesMapper.selectByExample(example);
        return ResultDTO.builder()
                .status(HttpStatus.OK.value())
                .data(shareImages).build();

    }

    /**
     * 根据ｓｈａｒｅｉｄ　获得分享的详情
     */
    public ResultDTO getdetailById(Integer id) {

        Share share = shareMapper.selectByPrimaryKey(id);
        return ResultDTO.builder()
                .data(share)
                .status(HttpStatus.OK.value()).build();
    }
}
