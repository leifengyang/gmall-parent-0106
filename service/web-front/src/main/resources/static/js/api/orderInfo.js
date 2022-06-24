var orderInfo = {

    api_name: '/api/order',

    getOrderDetail(orderDetailId) {
        return request({
            url: this.api_name + `/auth/getOrderDetail/${orderDetailId}`,
            method: 'get'
        })
    }
}
