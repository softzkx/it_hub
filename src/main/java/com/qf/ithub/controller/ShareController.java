package com.qf.ithub.controller;

import com.qf.ithub.common.aspect.annotation.CheckLogin;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/24 10:46
 * FileName: ShareController
 * Description: ${DESCRIPTION}
 */
@RestController
@RequestMapping("/shares")
@CrossOrigin(origins="*",maxAge = 3600)
public class ShareController {

    @Autowired
    private ShareService shareService;

    /**
     * 获得首页轮播的商品前5条
     */
    @GetMapping("/getlunbos")
    public ResultDTO getLunbos(){
        return shareService.getLunbos();
    }

    /**
     * 获得首页推荐的商品前10条
     */
    @GetMapping("/gethots")
    public ResultDTO getHots(){
        return shareService.getHots();
    }

    /***
     * 获得详情页上面 某个分享的图片的集合
     */
    @GetMapping("/getimages/{id}")
    public ResultDTO getImagesById(@PathVariable("id") Integer id){
        return shareService.getImagesById(id);
    }

    /**
     * 根据ｓｈａｒｅｉｄ　获得分享的详情
     */
    @GetMapping("/getdetail/{id}")
    public ResultDTO getdetailById(@PathVariable("id") Integer id){
        return shareService.getdetailById(id);
    }

    // 用户兑换指定的资源
    @PostMapping("/exchangeshare")
    @CheckLogin
    public ResultDTO exchangeshare(Integer shareid, HttpServletRequest request){
        // 1 获得userid
        Integer userid = (Integer) request.getAttribute("userid");

        return shareService.exchangeShare(userid,shareid);
    }
}
