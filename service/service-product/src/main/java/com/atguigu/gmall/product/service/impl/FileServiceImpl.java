package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.product.config.minio.MinioProperties;
import com.atguigu.gmall.product.service.FileService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    MinioClient minioClient;

    @Value("${app.minio.bucketName}")
    String bucketName;

    @Autowired
    MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) throws Exception {
        String filename = UUID.randomUUID().toString().replace("-","")+"_"+file.getOriginalFilename();

        //1、准备上传
        PutObjectOptions options = new PutObjectOptions(file.getSize(),-1);
        options.setContentType(file.getContentType());

        minioClient.putObject(bucketName,filename, file.getInputStream(),options);


        //2、返回这个资源的访问路径  minio服务器地址+
        //http://192.168.200.100:9000/gmall/123.jpg
        String url =  minioProperties.getEndpoint()+"/"+minioProperties.getBucketName()+"/"+filename;
        return url;
    }
}
