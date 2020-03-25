package com.qf.ithub.service;

import com.qf.ithub.common.dto.LoginByPhoneReqDTO;
import com.qf.ithub.common.dto.LoginRespDTO;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.common.utils.JwtOperatorHS256;
import com.qf.ithub.entity.User;
import com.qf.ithub.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/25 8:44
 * FileName: UserService
 * Description: ${DESCRIPTION}
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private JwtOperatorHS256 jwtOperatorHS256;

    /**
     * 电话号码 验证码登陆
     * @param
     * @return
     */
    public ResultDTO loginByPhone(LoginByPhoneReqDTO phone) {

        // 1 判断电话号码 在 用户表中是否存在
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("phone",phone.getPhonenum());
        User user = userMapper.selectOneByExample(example);
        // LoginRespDTO loginRespDTO = null;

        if (user == null) {
            user = User.builder()
                    .bonus(100)
                    .createTime(new Date())
                    .updateTime(new Date())
                    .isvip(false)
                    .phone(new Long(phone.getPhonenum()))
                    .roles("user")
                    .build();
            int count = userMapper.insertSelective(user);
            if(count!=1){
                throw AppException.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("添加用户失败").build();
            }
        }

        LoginRespDTO loginRespDTO = LoginRespDTO.builder()
                .bonus(user.getBonus())
                .id(user.getId())
                .token(createtoken(user))
                .isvip(user.getIsvip()).build();

        return ResultDTO.builder()
                .data(loginRespDTO)
                .status(HttpStatus.OK.value()).build();

    }

    private String createtoken(User user){
        // int i = 1/0;
        Map<String,Object> map = new HashMap<>();
        map.put("userid",user.getId());
        map.put("role",user.getRoles());
        map.put("wxid",user.getWxId());
        String token = this.jwtOperatorHS256.generateToken(map);
        return token;
    }
}
