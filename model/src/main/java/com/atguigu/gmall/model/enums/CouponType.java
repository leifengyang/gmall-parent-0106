package com.atguigu.gmall.model.enums;

import lombok.Getter;

@Getter
public enum CouponType {
    FULL_REDUCTION("满减"),
    FULL_DISCOUNT("满量打折" ),
    CASH("现金卷"),
    DISCOUNT("折扣卷" );

    private String comment ;

    CouponType(String comment ){
        this.comment=comment;
    }

    public static String getNameByType(String type) {
        CouponType arrObj[] = CouponType.values();
        for (CouponType obj : arrObj) {
            if (obj.name().equals(type)) {
                return obj.getComment();
            }
        }
        return "";
    }
}