package com.fade.global.component;

import org.springframework.web.multipart.MultipartFile;


public interface FileUploader {
    void upload(MultipartFile file, String path);
}
