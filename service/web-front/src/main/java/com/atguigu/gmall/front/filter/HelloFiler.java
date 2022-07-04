package com.atguigu.gmall.front.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloFiler implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        request.addHeader("token");
//        request.addHeader("UserId");
        //放行
//        filterChain.doFilter(servletRequest,servletResponse);
//        response.sendRedirect("http....");
//        filterChain.doFilter();
    }
}
