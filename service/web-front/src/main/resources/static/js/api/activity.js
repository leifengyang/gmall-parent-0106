var activity = {

    api_name: '/api/activity',

    findActivityAndCoupon(skuId) {
        return request({
            url: this.api_name + `/findActivityAndCoupon/` + skuId,
            method: 'get'
        })
    },

    getCouponInfo(couponId) {
        return request({
            url: this.api_name + `/auth/getCouponInfo/` + couponId,
            method: 'get'
        })
    },

    getPageList(page, limit) {
        return request({
            url: this.api_name + `/auth/${page}/${limit}`,
            method: 'get'
        })
    }
}
