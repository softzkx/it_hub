package com.qf.ithub.controller;

import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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

    /**
     * 获得手机验证码
     */
    @GetMapping("/getcheckcode")
    public ResultDTO getCheckCode(String phone, HttpSession session)  {
        try {
            ResultDTO resultDTO = messageService.sendRegSms(phone);
            session.setAttribute("checkcode",resultDTO.getData());
            return resultDTO;
        }catch (Exception ex){
            ex.printStackTrace();
            throw AppException.builder()
                    .message("发送短信失败，请待会再试")
                    .status(HttpStatus.BAD_GATEWAY.value()).build();
        }
    }
}
