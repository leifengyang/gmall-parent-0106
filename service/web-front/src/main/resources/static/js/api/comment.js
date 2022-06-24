var comment = {

    api_name: '/api/comment/commentInfo',

    save(commentInfo) {
        return request({
            url: this.api_name + `/auth/save`,
            method: 'post',
            data: commentInfo
        })
    },

    getPageList(skuId, page, limit) {
        return request({
            url: this.api_name + `/${skuId}/${page}/${limit}`,
            method: 'get'
        })
    }
}
