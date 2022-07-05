package com.atguigu.gmall.common.util;

//import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import com.atguigu.gmall.model.vo.user.UserAuth;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取登录用户信息类
 *
 */
public class AuthContextHolder {

    /**
     * 获取当前登录用户id
     * @param request
     * @return
     */
    public static String getUserId(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        return StringUtils.isEmpty(userId) ? "" : userId;
    }

    /**
     * 获取当前未登录临时用户id
     * @param request
     * @return
     */
    public static String getUserTempId(HttpServletRequest request) {
        String userTempId = request.getHeader("userTempId");
        return StringUtils.isEmpty(userTempId) ? "" : userTempId;
    }

    /**
     * 获取当前请求中携带的用户或临时id信息
     * @return
     */
    public static UserAuth getUserAuth() {
        //1、获取Spring给我们当前线程绑定好的请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String userId = request.getHeader("UserId");
        String userTempId = request.getHeader("UserTempId");
        UserAuth auth = new UserAuth();
        if(!StringUtils.isEmpty(userId)){
            auth.setUserId(Long.parseLong(userId));
        }

        auth.setTempId(userTempId);
        return auth;
    }
}
