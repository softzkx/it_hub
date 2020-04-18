package com.qf.ithub.service;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.enumeration.OrderStatusEnum;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.common.pay.AlipayConfig;
import com.qf.ithub.common.utils.IDUtils;
import com.qf.ithub.entity.PayInfo;
import com.qf.ithub.entity.TbOrder;
import com.qf.ithub.mapper.PayInfoMapper;
import com.qf.ithub.mapper.TbOrderMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/28 7:55
 * FileName: PayService
 * Description: ${DESCRIPTION}
 */
@Service
public class PayService {

    @Resource
    private PayInfoMapper payInfoMapper;
    @Resource
    private TbOrderMapper orderMapper;

    /**
     *  创建订单
     */
    public String create(Long orderid,Long phone) {
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = orderid+"";
        // 订单名称，必填
        String subject = "vip";
        // 付款金额，必填
        String total_amount = "0.01";
        // 商品描述，可空
        //String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
        // 超时时间 可空
        String timeout_express = "20m";
        // 销售产品码 必填
        String product_code = "QUICK_MSECURITY_PAY";
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
                AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        //model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(AlipayConfig.notify_url);
        // 设置同步地址
        alipay_request.setReturnUrl(AlipayConfig.return_url);

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = client.pageExecute(alipay_request).getBody();
            System.out.println(form);
            return form;
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            throw AppException.builder()
                    .status(HttpStatus.BAD_GATEWAY.value())
                    .message("支付请求未能发起成功，请稍后再试...")
                    .build();
        }
    }

    /**
     *  1 生成本地订单
     */
    public ResultDTO createNativeOrder(Long phone,Integer userid) {

        // 1 生成orderid
        long orderid = new IDUtils().nextId();
        // 2 生成 订单对象
        TbOrder tbOrder = TbOrder.builder().amount(new BigDecimal(0.01))
                .orderNo(orderid)
                .status(OrderStatusEnum.CREATE.toString())
                .subject(phone + " 购买vip")
                .userId(userid).build();
        int count = orderMapper.insertSelective(tbOrder);
        if(count!=1){
            throw AppException.builder()
                    .status(HttpStatus.BAD_GATEWAY.value())
                    .message("支付请求未能发起成功，请稍后再试...")
                    .build();
        }

        // 3 生成支付信息
        PayInfo payInfo = PayInfo.builder()
                .userId(userid)
                .payPlatform(1)
                .orderNo(orderid)
                .platformStatus(OrderStatusEnum.WAITING.toString())
                .updateTime(new Date())
                .createTime(new Date())
                .build();
        count = payInfoMapper.insertSelective(payInfo);
        if(count!=1){
            throw AppException.builder()
                    .status(HttpStatus.BAD_GATEWAY.value())
                    .message("支付请求未能发起成功，请稍后再试...")
                    .build();
        }
        return ResultDTO.builder()
                .status(HttpStatus.OK.value())
                .data(orderid+"")
                .build();

    }
}
