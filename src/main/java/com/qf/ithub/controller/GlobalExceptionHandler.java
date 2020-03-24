package com.qf.ithub.controller;

import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/10 9:03
 * FileName: GlobalExceptionHandler
 * Description: ${DESCRIPTION}git
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResultDTO> secrityHandler(AppException ex){
        // log.warn("发生安全错误",ex);
        ResponseEntity responseBody = new ResponseEntity(
                ResultDTO.builder().message(ex.getMessage())
                .status(ex.getStatus())
                .returnCode(ex.getReturnCode())
                .build(),
                HttpStatus.valueOf(ex.getStatus())
        );
        return responseBody;

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultDTO> ExcptionHandler(Exception ex){
        ResponseEntity responseBody = new ResponseEntity(
                ResultDTO.builder()
                        .message("未知错误 请联系管理员")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .returnCode("SYSTEMERR001")
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return responseBody;

    }
}
