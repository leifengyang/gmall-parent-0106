package com.atguigu.gmall.model.vo.user;

import lombok.Data;

@Data
public class LoginSuccessRespVo {
    //token，userInfo：{nickName}
    private String token;
//    private Map<String,String> userInfo; //nickName
    private String nickName;
}
