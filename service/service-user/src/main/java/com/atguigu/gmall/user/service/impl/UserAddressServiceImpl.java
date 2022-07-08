package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.model.vo.user.UserAuth;
import com.atguigu.gmall.user.mapper.UserAddressMapper;
import com.atguigu.gmall.user.service.UserAddressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lfy
* @description 针对表【user_address(用户地址表)】的数据库操作Service实现
* @createDate 2022-07-04 11:25:45
*/
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService{

    @Autowired
    UserAddressMapper addressMapper;

    @Override
    public List<UserAddress> getUserAddress() {
        UserAuth auth = AuthContextHolder.getUserAuth();
        Long userId = auth.getUserId();

        QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);

        return addressMapper.selectList(wrapper);
    }
}




