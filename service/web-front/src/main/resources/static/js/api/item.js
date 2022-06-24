var item = {

    api_name : '/api/item',

  // 獲取sku詳細信息
  get(skuId) {
    return request({
      url: this.api_name + '/' + skuId,
      method: 'get'
    })
  },

  //獲取sku基本信息
  getSkuInfo(skuId) {
    return request({
      url: this.api_name + '/getSkuInfo/' + skuId,
      method: 'get'
    })
  }
}
