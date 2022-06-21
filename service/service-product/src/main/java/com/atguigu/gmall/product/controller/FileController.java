package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/admin/product")
public class FileController {


    @Autowired
    FileService fileService;


    /**
     * @RequestPart("file") 专门接受文件项
     * 总结：
     * 1）、前端：
     *            密码：   <input type="password" name="pwd"/> <br/>
     *            头像： <input type="file" name="haha" /> <br/>
     *      后台： @RequestParam("pwd")String pwd,@RequestPart("haha")MultipartFile file,
     *
     * 2）、前端：
     *        头像： <input type="file" name="header" /> <br/>
     *        身份证： <input type="file" name="sfz" /> <br/>
     *      后台： @RequestPart("header")MultipartFile header,@RequestPart("sfz")MultipartFile sfz
     * 3）、前端：
     *      头像： <input type="file" name="header" multiple/> <br/>
     *      身份证： <input type="file" name="sfz" /> <br/>
     *     后台： @RequestPart("header")MultipartFile[] header,@RequestPart("sfz")MultipartFile sfz
     * 4）、前端：
     *         不定项：不知道有多少个 input type="file"，不知道每个人的 name="header"
     *      后台：@RequestPart MultipartFile[] files； //只要是文件项，全都要
     *
     * 5）、前端：
     *       <input type="password" name="pwd"/>，<input type="password" name="aa"/> ...
     *       @RequestParam Map params： key就是参数名，value就是值
     *       Map<String, String[]> map = request.getParameterMap();
     *
     *
     * @param request
     * @param file
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @PostMapping("/fileUpload")
    public Result upload(HttpServletRequest request,
                         @RequestPart("file") MultipartFile file) throws Exception {
        //1、文件上传到minio，并拿到文件访问地址
        String url = fileService.upload(file);

//        //username,pwd,file
//        /**
//         *        用户名： <input type="text" name="username"/> <br/>
//         *        密码：   <input type="password" name="pwd"/> <br/>
//         * @RequestParam("")  getParameter
//         */
//        Enumeration<String> names = request.getParameterNames();
//
//        while (names.hasMoreElements()) {
//            String element = names.nextElement();
//            System.out.println(element);
//
//        }
//
//        /**
//         * @RequestPart
//         */
//        Collection<Part> parts = request.getParts();
//        for (Part part : parts) {
//            System.out.println("part:"+part.getName());
//        }
//
//        System.out.println(names);
//        String url = "";
        return Result.ok(url);
    }
}
