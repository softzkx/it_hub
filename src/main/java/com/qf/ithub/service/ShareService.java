package com.qf.ithub.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.ithub.common.dto.ChartAllDTO;
import com.qf.ithub.common.dto.EditSharesDTO;
import com.qf.ithub.common.dto.ImgDTO;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.entity.*;
import com.qf.ithub.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

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
    @Resource
    private ShareCategoryMapper shareCategoryMapper;

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

    /**
     * 获得 所有的资源种类
     */
    public ResultDTO getCats() {

        List<ShareCategory> shareCategories = shareCategoryMapper.selectAll();
        return ResultDTO.builder()
                .status(HttpStatus.OK.value())
                .data(shareCategories)
                .build();
    }

    /**
     * 根据种类id 获得资源 和 分页信息获得资源的集合
     */
    public ResultDTO getSharesByCatid(Integer catid, Integer pageno) {
        // 1 如果catid = -1 查询全部 否则 按照 catid 查询
        PageHelper.startPage(pageno,10);
        List<Share> shares = null;
        Example example = new Example(Share.class);
        if(catid!=-1){
            example.createCriteria().andEqualTo("catid",catid);

        }
        example.setOrderByClause("create_time desc");
        shares = shareMapper.selectByExample(example);
        PageInfo<Share> pageInfo = new PageInfo<>(shares);
        return ResultDTO.builder()
                .status(HttpStatus.OK.value())
                .data(pageInfo).build();
    }

    /**
     * 获得指定用户兑换过的资源
     */
    public ResultDTO getExchangeShares(Integer userid, Integer pageno,Integer pagesize){
        PageHelper.startPage(pageno,pagesize);
        List<Share> exchangeShares = shareMapper.getExchangeShares(userid);
        PageInfo<Share> of = new PageInfo<>(exchangeShares);
        return ResultDTO.builder()
                .data(of)
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * 获得指定用户贡献的资源
     */
    public ResultDTO getUpShares(Integer userid, Integer pageno,Integer pagesize) {

        PageHelper.startPage(pageno,pagesize);
        Example example = new Example(Share.class);
//        example.createCriteria().andEqualTo("userId",userid)
//                .andEqualTo("auditStatus","PASS");
        example.createCriteria().andEqualTo("userId",userid);
        example.setOrderByClause("update_time desc");
        List<Share> shares = shareMapper.selectByExample(example);
        PageInfo<Share> of = new PageInfo<>(shares);
        return ResultDTO.builder()
                .data(of)
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * 更新shares
     * @param editSharesDTO
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public ResultDTO updateShares(EditSharesDTO editSharesDTO) {

        // 1 更新信息
        Share share = Share.builder().build();
        BeanUtils.copyProperties(editSharesDTO,share);
        share.setUpdateTime(new Date());
        int count = shareMapper.updateByPrimaryKeySelective(share);
        if (count != 1) {
            throw AppException.builder()
                    .status(HttpStatus.BAD_GATEWAY.value())
                    .message("更新分享信息失败...")
                    .build();
        }
        // 2 删除图片
        Example example = new Example(ShareImages.class);
        example.createCriteria().andEqualTo("shareid",editSharesDTO.getId());
        shareImagesMapper.deleteByExample(example);
        // 3 添加图片
        for (ImgDTO imgDTO : editSharesDTO.getImageList()) {
            ShareImages shareImage = ShareImages.builder()
                    .shareid(editSharesDTO.getId())
                    .image(imgDTO.getUrl()).build();
            count = shareImagesMapper.insertSelective(shareImage);
            if (count != 1) {
                throw AppException.builder()
                        .status(HttpStatus.BAD_GATEWAY.value())
                        .message("添加图片失败...")
                        .build();
            }
        }

        return ResultDTO.builder()
                .status(HttpStatus.OK.value()).build();
    }

    /**
     * 添加shares
     * @param editSharesDTO
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public ResultDTO addShares(EditSharesDTO editSharesDTO) {
        // 1 添加信息
        Share share = Share.builder().build();
        BeanUtils.copyProperties(editSharesDTO,share);
        share.setBuyCount(0);
        share.setAuditStatus("NOT_YET");
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        int count = shareMapper.insertSelective(share);
        if(count!=1){
            throw AppException.builder()
                    .status(HttpStatus.BAD_GATEWAY.value())
                    .message("添加分享信息失败...")
                    .build();
        }
        // 2 添加图片
        for (ImgDTO imgDTO : editSharesDTO.getImageList()) {
            ShareImages shareImage = ShareImages.builder()
                    .shareid(share.getId())
                    .image(imgDTO.getUrl()).build();
            count = shareImagesMapper.insertSelective(shareImage);
            if (count != 1) {
                throw AppException.builder()
                        .status(HttpStatus.BAD_GATEWAY.value())
                        .message("添加图片失败...")
                        .build();
            }
        }

        return ResultDTO.builder()
                .status(HttpStatus.OK.value()).build();
    }

    /**
     * 获得所有未审核的资源
     */
    public ResultDTO getNotYetShares(Integer pageno , Integer pageSize){
        PageHelper.startPage(pageno,pageSize);
        Example example = new Example(Share.class);
        example.createCriteria().andEqualTo("auditStatus","NOT_YET");
        example.setOrderByClause("update_time desc");
        List<Share> shares = shareMapper.selectByExample(example);
        PageInfo<Share> pageInfo = new PageInfo<>(shares);
        return ResultDTO.builder()
                .message("")
                .status(200)
                .data(pageInfo).build();
    }

    /**
     *  审核指定的资源
     */
    public ResultDTO aduitShares(Share share) {
        // 1 更新状态 和 原因
        int count = shareMapper.updateByPrimaryKeySelective(share);
        if (count != 1) {
            throw AppException.builder()
                    .status(700)
                    .message("审核资源失败.")
                    .build();
        }
        // 2 如果是通过了 要给用户加分
        Integer userId = share.getUserId();
        // 2.1 获得用户
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw AppException.builder()
                    .status(700)
                    .message("用户不存在")
                    .build();
        }
        user.setBonus(user.getBonus()+share.getPrice());
        count = userMapper.updateByPrimaryKeySelective(user);
        if (user == null) {
            throw AppException.builder()
                    .status(700)
                    .message("更新用户积分失败")
                    .build();
        }

        return ResultDTO.builder().status(200).message("审核完成。。").build();
    }

    /**
     *  获得首页全局报表的数据
     */
    public ResultDTO getChartsAll() {
        List<ChartAllDTO> chartAllDTO = shareMapper.getChartAllDTO();
        // 1 x 抽的数组
        List<String> x = new ArrayList<>();
        // 2 1y 抽的数组
        List<Integer> y = new ArrayList<>();

        for (ChartAllDTO cd : chartAllDTO) {
            x.add(cd.getName());
            y.add(cd.getCount());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("x",x);
        map.put("y",y);

        return ResultDTO.builder()
                .message("")
                .status(200)
                .data(map)
                .build();
    }
}
