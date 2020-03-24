package com.qf.ithub.controller;

import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
