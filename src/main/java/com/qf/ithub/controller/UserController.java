package com.qf.ithub.controller;

import com.qf.ithub.common.dto.LoginByPhoneReqDTO;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.service.MessageService;
import com.qf.ithub.service.MidUserShareService;
import com.qf.ithub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/24 18:56
 * FileName: UserController
 * Description: ${DESCRIPTION}
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(origins="*",maxAge = 3600)
public class UserController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MidUserShareService midUserShareService;

    /**
     * 获得手机验证码
     */
    @GetMapping("/getcheckcode")
    public ResultDTO getCheckCode(String phone, HttpSession session)  {
        try {
            ResultDTO resultDTO = messageService.sendRegSms(phone);
            // session.setAttribute("checkcode",resultDTO.getData());
            // session.setMaxInactiveInterval(120);
            String uuidkey = UUID.randomUUID().toString();
            resultDTO.setMessage(uuidkey);
            // uuidkey = uuidkey + resultDTO.getData()
            // System.out.println("set key:" + uuidkey);
            // System.out.println("set value:" + resultDTO.getData());
            redisTemplate.opsForValue().set(uuidkey,resultDTO.getData(),2, TimeUnit.MINUTES);
            // resultDTO.
            return resultDTO;
        }catch (Exception ex){
            ex.printStackTrace();
            throw AppException.builder()
                    .message("发送短信失败，请待会再试")
                    .status(HttpStatus.BAD_GATEWAY.value()).build();
        }
    }

    /**
     *  电话号码 验证码登陆
     */
    @PostMapping("/loginbyphone")
    public ResultDTO loginByPhone(@RequestBody LoginByPhoneReqDTO loginByPhoneReqDTO,HttpSession session,@RequestHeader("token") String token){
    // public ResultDTO loginByPhone(@RequestParam("phonenum") String phonenum ,@RequestParam("checkcode") String checkcode){
        // 1 判断验证码是否过期
        Object obj = redisTemplate.opsForValue().get(token);

        // System.out.println("get key:" + token);
        // System.out.println("get value:" + obj.toString());

        if(obj==null || !loginByPhoneReqDTO.getCheckcode().equals(obj.toString())){
            return ResultDTO.builder()
                    .status(HttpStatus.MULTI_STATUS.value())
                    .message("验证码已经过期，请重新获得验证码").build();
        }
        // 2 让service 处理
        return userService.loginByPhone(loginByPhoneReqDTO);
    }

    /**
     *  根据userid 和 shareid 查询当前用户是否购买过这个资源
     */
    @GetMapping("/getmus")
    public ResultDTO getMidUserShare(@RequestParam("userid") Integer userid, @RequestParam("shareid") Integer shareid){
        return midUserShareService.getMidUserShare(userid,shareid);
    }


}
