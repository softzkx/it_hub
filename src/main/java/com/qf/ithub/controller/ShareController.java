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
        System.out.println(shareid);
        Integer userid = (Integer) request.getAttribute("userid");

        return shareService.exchangeShare(userid,shareid);
    }

    /**
     * 获得 所有的资源种类
     */
    @GetMapping("/getcats")
    public ResultDTO getCats(){
       return shareService.getCats();
    }

    /**
     * 根据种类id 获得资源 和 分页信息获得资源的集合
     */
    @GetMapping("/bycatid")
    public ResultDTO getSharesByCatid(
            @RequestParam(name = "catid") Integer catid,
            @RequestParam(name="pageno",defaultValue = "1") Integer pageno
    ){
        return shareService.getSharesByCatid(catid,pageno);
    }

    /**
     *  根据用户id 获得用户的兑换资源集合 或者 共享资源的集合
     */
    @GetMapping("/getsharesbytype")
    @CheckLogin
    public ResultDTO getSharesbyType(HttpServletRequest request,
                                    @RequestParam("isexchange") Boolean isExchange,
                                     @RequestParam(name="pageno",defaultValue = "1")Integer pageno){
        Integer userid = (Integer) request.getAttribute("userid");
        if(isExchange){
            // 返回我兑换过的资源集合
            return  shareService.getExchangeShares(userid,pageno);
        }
        // 返回我up 的资源
        return  shareService.getUpShares(userid,pageno);
    }
}
