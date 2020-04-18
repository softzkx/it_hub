package com.qf.ithub.controller;

import com.qf.ithub.common.dto.ResultDTO;
import com.qf.ithub.common.exception.AppException;
import com.qf.ithub.common.utils.OSSSingleUtil;
import com.qf.ithub.config.OSSConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Copyright (C), 2017-2020, 扩新工作室
 * Author: zoukx
 * Date: 2020/3/16 15:41
 * FileName: UoloadController
 * Description: ${DESCRIPTION}
 */
@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin(origins = "*",maxAge = 3600)
public class UoloadController {

    @Autowired
    private OSSConfig ossConfig;

    @PostMapping("/upload")
    public ResultDTO uploadOSS(@RequestParam(value = "file") MultipartFile file){
        // 1 获得文件的后罪名
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));

        String ossFileUrlSingle = null;
        try {
            ossFileUrlSingle = OSSSingleUtil.upload(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret(), ossConfig.getBucketName(), ossConfig.getUrl(), file.getInputStream(),
                    "upload/demo", fileSuffix);
            return ResultDTO.builder()
                    .status(HttpStatus.OK.value())
                    .data(ossFileUrlSingle)
                    .build();
        } catch (Exception e) {
            throw AppException.builder()
                    .message("上传文件失败..[filename=]" + file.getName())
                    .returnCode("errupload001")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }
}
