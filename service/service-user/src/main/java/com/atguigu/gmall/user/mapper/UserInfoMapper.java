package com.atguigu.gmall.user.mapper;

import com.atguigu.gmall.model.user.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author lfy
* @description 针对表【user_info(用户表)】的数据库操作Mapper
* @createDate 2022-07-04 11:25:45
* @Entity com.atguigu.gmall.user.domain.UserInfo
*/
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo getUserByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("passwd") String passwd);
}




