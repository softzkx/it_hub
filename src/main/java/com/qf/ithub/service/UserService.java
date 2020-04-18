package com.qf.ithub.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.ithub.common.dto.LoginByPhoneReqDTO;
import com.qf.ithub.common.dto.LoginRespDTO;
import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.dto.UserListDTO;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.common.utils.JwtOperatorHS256;
import com.qf.ithub.entity.User;
import com.qf.ithub.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

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
     *  用户名和密码登陆
     */
    public ResultDTO loginByUserName(User user) {

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("userName",user.getUserName());
        User user1 = userMapper.selectOneByExample(example);
        if (user1 == null) {
            throw AppException.builder()
                    .status(600)
                    .message("用户名不存在").build();
        }

        if(user1.getUserPassword().equals(user.getUserPassword())==false){
            throw AppException.builder()
                    .status(600)
                    .message("用户名或者密码错误").build();
        }

        LoginRespDTO loginRespDTO = LoginRespDTO.builder()
                .bonus(user1.getBonus())
                .id(user1.getId())
                .token(createtoken(user1))
                .roles(user1.getRoles())
                .isvip(user1.getIsvip()).build();

        return ResultDTO.builder()
                .message("登录成功")
                .status(200)
                .data(loginRespDTO)
                .build();
    }

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
                    .roleid(2)
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
                .roles(user.getRoles())
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
        map.put("roleid",user.getRoleid());
        map.put("wxid",user.getWxId());
        String token = this.jwtOperatorHS256.generateToken(map);
        return token;
    }

    /**
     *  获得用户列表指定页码
     */
    public ResultDTO getUserInfo(String phone,Integer roleid,Boolean isvip,Integer pageno,Integer pagesize){
        PageHelper.startPage(pageno,pagesize);
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(phone)){
            criteria.andLike("phone","%"+phone+"%");
        }
        if(roleid!=null){
            criteria.andEqualTo("roleid",roleid);
        }
        if(isvip!=null){
            criteria.andEqualTo("isvip",isvip);
        }
        List<User> users = userMapper.selectByExample(example);
        List<UserListDTO> users1 = new ArrayList<>();
        for (User user : users) {
            UserListDTO userListDTO = new UserListDTO();
            BeanUtils.copyProperties(user,userListDTO);
            userListDTO.setIsEdit(false);
            users1.add(userListDTO);
        }
        PageInfo<User> pageInfo1 = new PageInfo<>(users);
        PageInfo<UserListDTO> pageInfo = new PageInfo<>(users1);
        pageInfo.setTotal(pageInfo1.getTotal());
        return ResultDTO.builder()
                .data(pageInfo)
                .status(200).build();

    }

    /**
     *  更新指定用户的角色
     */
    public ResultDTO updroleByUserid(Integer userid, Integer roleid,String rolename) {
        User user = User.builder().roleid(roleid).id(userid).roles(rolename).build();
        int count = userMapper.updateByPrimaryKeySelective(user);
        if(count!=1){
            throw AppException.builder()
                    .status(600)
                    .message("修改用户的角色失败").build();
        }
        return ResultDTO.builder()
                .message("修改用户的角色成功").status(200).build();
    }

    /**
     *  获得用户的信息
     */
    public ResultDTO getUserinfo(Integer userid) {

        User user = userMapper.selectByPrimaryKey(userid);
        if (user == null) {
            throw AppException.builder()
                    .status(600)
                    .message("用户不存在").build();
        }

        return ResultDTO.builder()
                .status(200)
                .data(user).build();
    }

    /**
     *  根据用户名查询用户的信息
     */
    public ResultDTO getUserInfoByName(String userName,Integer id) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("userName",userName);
        User user = userMapper.selectOneByExample(example);
        boolean flag = true;
        // 如果用这个名字能查出一个人来 而且这个人 不是当前修改的人 那么返回false 意味着不能验证通过
        if(user!=null && user.getId()!=id){
            flag = false;
        }
        return ResultDTO.builder().status(200).data(flag).build();
    }

    /**
     *  更新用户信息
     */
    public ResultDTO updateUser(User user) {

        int count = userMapper.updateByPrimaryKeySelective(user);
        if(count!=1){
            throw AppException.builder()
                    .status(600)
                    .message("用户编辑失败").build();
        }
        return ResultDTO.builder()
                .status(200)
                .message("用户编辑成功")
                .build();
    }


}
