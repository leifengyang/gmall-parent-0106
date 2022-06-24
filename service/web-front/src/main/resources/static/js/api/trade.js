var trade = {

    api_name : '/api/order',

  // 添加购物车
  trade() {
    return request({
      url: this.api_name + '/auth/trade',
      method: 'get'
    })
  },

  // 提交订单
  submitOrder(order, tradeNo) {
    return request({
      url: this.api_name + '/auth/submitOrder?tradeNo=' + tradeNo,
      method: 'post',
      data: order
    })
  },

  // 获取订单
  getPayOrderInfo(orderId) {
    return request({
      url: this.api_name + '/auth/getPayOrderInfo/' + orderId,
      method: 'get'
    })
  },

  // 获取订单
  getOrderPageList(page, limit) {
    return request({
      url: this.api_name + `/auth/${page}/${limit}`,
      method: 'get'
    })
  }
}
