package com.atguigu.gmall.user.service;

import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.user.LoginSuccessRespVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lfy
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2022-07-04 11:25:45
*/
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户登录
     * @param userInfo
     * @param ipAddress
     * @return
     */
    LoginSuccessRespVo login(UserInfo userInfo, String ipAddress);

    /**
     *
     * @param token
     */
    void logout(String token);
}
