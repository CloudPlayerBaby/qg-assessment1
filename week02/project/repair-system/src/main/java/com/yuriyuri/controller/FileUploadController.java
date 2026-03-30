package com.yuriyuri.controller;

import com.yuriyuri.common.Result;
import com.yuriyuri.util.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "文件上传", description = "文件上传相关接口")
@RestController
public class FileUploadController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @Operation(summary = "图片上传",description = "上传到阿里云服务器并返回url地址")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        String origin = file.getOriginalFilename();
        String filename = null;
        if (origin != null) {
            filename = UUID.randomUUID() +origin.substring(origin.lastIndexOf("."));
        }
        String url = aliOssUtil.uploadFile(filename, file.getInputStream());
        return Result.success(url);
    }
}
