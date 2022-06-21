package com.atguigu.gmall.product.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 上传前端提交的文件到Minio
     * @param file
     * @return
     */
    String upload(MultipartFile file) throws Exception;
}
