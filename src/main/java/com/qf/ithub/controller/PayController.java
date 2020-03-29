package com.qf.ithub.controller;

import com.qf.ithub.common.aspect.annotation.CheckLogin;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.common.pay.AlipayConfig;
import com.qf.ithub.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/28 7:41
 * FileName: PayController
 * Description: ${DESCRIPTION}
 */
@Controller
@RequestMapping("/pay")
@CrossOrigin(origins="*",maxAge = 3600)
public class PayController {


    @Autowired
    private PayService payService;
    /**
     * 创建订单
     * @param phone  由于只需要支付一笔 终身成为vip 本系统采用电话号码作为订单号
     */
    @GetMapping("/create")
    // @CheckLogin
    public void create(Long orderid, Long phone,HttpServletResponse response) {
        System.out.println(orderid + ":" + phone);
        String form = payService.create(orderid,phone);
        response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
        try {
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        }catch (Exception ex){
            throw AppException.builder()
                    .status(HttpStatus.BAD_GATEWAY.value())
                    .message("支付请求未能发起成功，请稍后再试...")
                    .build();
        }

    }

    /**
     *  1 生成本地订单
     */
    @CheckLogin
    @PostMapping("/native")
    @ResponseBody
    public ResultDTO createNativeOrder(Long phone , HttpServletRequest request){
        // System.out.println(phone + ":" + payPlatform);
        Integer userid = (Integer) request.getAttribute("userid");
        return payService.createNativeOrder(phone,userid);

    }

    @ResponseBody
    @PostMapping("/notify")
    public String notifyfromAlipay(){
        System.out.println("回来了。。。");
        return "success";

    }
}
