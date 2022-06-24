var favorInfo = {

    api_name: '/api/user/favorInfo',

    add(favorInfo) {
        return request({
            url: this.api_name + `/auth/add`,
            method: 'post',
            data: favorInfo
        })
    },

    isFavor(skuId) {
        return request({
            url: this.api_name + `/isFavor/` + skuId,
            method: 'get'
        })
    },

    cancel(id) {
        return request({
            url: this.api_name + `/auth/cancel/` + id,
            method: 'get'
        })
    },

    getPageList(page, limit) {
        return request({
            url: this.api_name + `/auth/${page}/${limit}`,
            method: 'get'
        })
    },
}
