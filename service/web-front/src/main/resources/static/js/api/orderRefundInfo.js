var orderRefundInfo = {

    api_name: '/api/order/orderRefundInfo',

    getPageList(page, limit) {
        return request({
            url: this.api_name + `/auth/${page}/${limit}`,
            method: 'get'
        })
    },

    save(orderRefundInfo) {
        return request({
            url: this.api_name + `/auth/save`,
            method: 'post',
            data: orderRefundInfo
        })
    },

    delevered(id, trackingNo) {
        return request({
            url: this.api_name + `/auth/delevered/${id}/${trackingNo}`,
            method: 'get'
        })
    }
}
