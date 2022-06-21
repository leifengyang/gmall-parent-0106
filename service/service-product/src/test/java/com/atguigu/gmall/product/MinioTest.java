package com.atguigu.gmall.product;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;

@SpringBootTest
public class MinioTest {

    @Autowired
    MinioClient minioClient;

    @Test
    void testUpload2() throws Exception {
        FileInputStream stream = new FileInputStream("C:\\Users\\53409\\OneDrive\\图片\\Saved Pictures\\ServerDir\\Camera Roll4-191010163513.jpg");
        PutObjectOptions options = new PutObjectOptions(stream.available(),-1L);
        options.setContentType("image/jpeg");
        minioClient.putObject("gmall","Camera Roll4-191010163513.jpg",stream,options);
        System.out.println("上传完成");
    }



    @Test
    void testUpload() {
        try {
            //1、使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(
                    "http://192.168.200.100:9000",
                    "admin",
                    "admin123456");
            System.out.println(minioClient);

            //2、准备一个bucket，接受文件
            boolean gmall = minioClient.bucketExists("gmall");
            if(!gmall){
                //桶不存在就创建一个新的
                minioClient.makeBucket("gmall");
                System.out.println("gmall不存在，但是创建好了");
            }

            //3、上传
            FileInputStream stream = new FileInputStream("C:\\Users\\53409\\OneDrive\\图片\\Saved Pictures\\ServerDir\\123.jpg");
            //上传的一些参数项设置
            PutObjectOptions options = new PutObjectOptions(stream.available(),-1L);
            options.setContentType("image/jpeg");
            minioClient.putObject("gmall","123.jpg",stream,options);

            //http://192.168.200.100:9000/gmall/1.jpg
            System.out.println("上传成功：访问地址：http://192.168.200.100:9000/gmall/123.jpg");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }


    }
}
