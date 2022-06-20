package com.atguigu.gmall.model.enums;

import lombok.Getter;

@Getter
public enum CouponRangeType {
    SPU("单品(spu)"),
    CATAGORY("品类券" ),
    TRADEMARK("品牌券");

    private String comment ;

    CouponRangeType(String comment ){
        this.comment=comment;
    }

    public static String getNameByType(String type) {
        CouponRangeType arrObj[] = CouponRangeType.values();
        for (CouponRangeType obj : arrObj) {
            if (obj.name().equals(type)) {
                return obj.getComment();
            }
        }
        return "";
    }

}