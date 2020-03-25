package com.qf.ithub.common.aspect;


import com.qf.ithub.common.aspect.annotation.CheckRight;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.common.utils.JwtOperatorHS256;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/10 7:29
 * FileName: CheckLoginAspect
 * Description: ${DESCRIPTION}
 */
@Component
@Aspect
@Slf4j
public class AuthAspect {

    @Autowired
    private JwtOperatorHS256 jwtOperatorHS256;

    @Around("@annotation(com.qf.ithub.common.aspect.annotation.CheckLogin)")
    public Object aroundLoginCheck(ProceedingJoinPoint joinPoint) throws Throwable {

        checkLogin();
        Object proceed = joinPoint.proceed();
        return proceed;
    }

    @Around("@annotation(com.qf.ithub.common.aspect.annotation.CheckRight)")
    public Object aroundAuthCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1 上来先 检查是否有token 如果token 有问题 直接排除异常（LOGINERROR001）
        checkLogin();

        // 2 再验证权限 如果能走到这一步 request 中是应该有角色的
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String role = request.getAttribute("role").toString();
        // 3 验证token 中带来的用户的权限是否和 注解中需要的权限能匹配上
        // 3.1 获得注解中的权限
        MethodSignature method = (MethodSignature) joinPoint.getSignature();

        CheckRight annotation = method.getMethod().getAnnotation(CheckRight.class);
        String needhasRole = annotation.value();
        if(Objects.equals(needhasRole,role)==false){
            throw AppException.builder().status(HttpStatus.UNAUTHORIZED.value())
                    .message("没有权限使用...")
                    .returnCode("LOGINERROR002")
                    .build();
        }
        // 4 执行原来的逻辑
        Object proceed = joinPoint.proceed();

        return proceed;

    }

    private void checkLogin() {
        String token =null;
        try {
            // 1 从header 中获得token
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            token = request.getHeader("X-Token");
            log.info("token {}" + token);
            // 2 token 不存在 或者不合法（篡改 过期）
            Boolean validateToken = jwtOperatorHS256.validateToken(token);

            Claims claimsFromToken = jwtOperatorHS256.getClaimsFromToken(token);
            request.setAttribute("userid",claimsFromToken.get("userid"));
            request.setAttribute("role",claimsFromToken.get("role"));
            request.setAttribute("wxid",claimsFromToken.get("wxid"));

        } catch (Throwable throwable) {
            if (token != null) {
                throw AppException.builder().status(HttpStatus.UNAUTHORIZED.value())
                        .message("您的登陆状态已经失效，请重新登陆...")
                        .returnCode("LOGINERROR001")
                        .build();
            }else{
                throw AppException.builder().status(HttpStatus.UNAUTHORIZED.value())
                        .message("请您先登录再使用。。。")
                        .returnCode("LOGINERROR001")
                        .build();
            }

        }
    }
}
