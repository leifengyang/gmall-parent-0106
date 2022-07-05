package com.atguigu.gmall.common.interceptor;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class RequestHeaderSetFeignIntercetpor implements RequestInterceptor{

    @Override
    public void apply(RequestTemplate template) {
        //拿到老请求
//        HttpServletRequest request = CartController.threadLocal.get(Thread.currentThread());
//        System.out.println("哈哈");
        //TODO 那种情况下，拿到当前线程的请求为NULL
        //1、先从Spring给我们提供的RequestContextHolder 中拿到当前线程的请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        //2、拿到当前请求
        HttpServletRequest request = attributes.getRequest();

        //3、把原来所有的请求头继续放到 template 中，方便feign远程调用创建请求之前能得到所有的头
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            //请求头名
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if("UserTempId".equalsIgnoreCase(headerName) || "userId".equalsIgnoreCase(headerName) ){
                template.header(headerName,headerValue);
            }
        }
//        template.header()

    }
}
