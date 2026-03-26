package com.yuriyuri.controller;

import com.yuriyuri.common.Result;
import com.yuriyuri.util.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileUploadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        String origin = file.getOriginalFilename();
        String filename = null;
        if (origin != null) {
            filename = UUID.randomUUID() +origin.substring(origin.lastIndexOf("."));
        }
        String url = AliOssUtil.uploadFile(filename, file.getInputStream());
        return Result.success(url);
    }
}
