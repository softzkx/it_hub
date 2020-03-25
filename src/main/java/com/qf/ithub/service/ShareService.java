package com.qf.ithub.service;

import com.github.pagehelper.PageHelper;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.entity.MidUserShare;
import com.qf.ithub.entity.Share;
import com.qf.ithub.entity.ShareImages;
import com.qf.ithub.entity.User;
import com.qf.ithub.mapper.MidUserShareMapper;
import com.qf.ithub.mapper.ShareImagesMapper;
import com.qf.ithub.mapper.ShareMapper;
import com.qf.ithub.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
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
    private UserMapper userMapper;
    @Resource
    private MidUserShareMapper midUserShareMapper;
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

    @Transactional(rollbackFor=Exception.class)
    public ResultDTO exchangeShare(Integer userid, Integer shareid) {

        // 1 首先查看用户是否兑换过该资源
        Example example = new Example(MidUserShare.class);
        example.createCriteria().andEqualTo("shareId",shareid)
                .andEqualTo("userId",userid);
        MidUserShare midUserShare = midUserShareMapper.selectOneByExample(example);
        // 1.1 如果已经兑换过 返回成功即可
        if (midUserShare != null) {
            return ResultDTO.builder()
                    .message("您已经兑换过，无需再兑换")
                    .status(HttpStatus.OK.value())
                    .build();
        }

        // 2 获得用户的状态
        User user = userMapper.selectByPrimaryKey(userid);
        Share share = shareMapper.selectByPrimaryKey(shareid);
        if (user==null){
            throw AppException.builder()
                    .message("用户信息不存在")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        // TODO 这个地方改成乐观锁要好些?
        if (share==null){
            throw AppException.builder()
                    .message("资源信息不存在")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        // 3 如果用户没兑换过 看用户是不是vip 如果是vip 只需要添加记录和 修改share 被分享的次数
        if(user.getIsvip()==false){
            if(user.getBonus()<share.getPrice()){
                throw AppException.builder()
                        .message("您的积分不够 请升级成vip,或者赚取积分")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build();
            }

            // 扣去积分
            user.setBonus(user.getBonus() - share.getPrice());
            int count = userMapper.updateByPrimaryKeySelective(user);
            if(count!=1){
                throw AppException.builder()
                        .message("兑换失败（扣积分失败）")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build();
            }
        }

        // 修改share 被修改的次数
        share.setBuyCount(share.getBuyCount()+1);
        int count = shareMapper.updateByPrimaryKey(share);
        if(count!=1){
            throw AppException.builder()
                    .message("兑换失败")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        // 添加一条兑换记录
        midUserShare= MidUserShare.builder()
                .shareId(shareid)
                .userId(userid)
                .build();
        count = midUserShareMapper.insertSelective(midUserShare);
        if(count!=1){
            throw AppException.builder()
                    .message("兑换失败（增加兑换记录失败）")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        return ResultDTO.builder().status(HttpStatus.OK.value()).message("兑换成功").build();


    }
}
