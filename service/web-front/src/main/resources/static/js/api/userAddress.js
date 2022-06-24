var userAddress = {

    api_name: '/api/user/userAddress',

    save(userAddress) {
        return request({
            url: this.api_name + `/auth/save`,
            method: 'post',
            data: userAddress
        })
    },

    update(userAddress) {
        return request({
            url: this.api_name + `/auth/update`,
            method: 'post',
            data: userAddress
        })
    },

    delete(userAddressId) {
        return request({
            url: this.api_name + `/auth/delete/` + userAddressId,
            method: 'get'
        })
    },

    findUserAddress() {
        return request({
            url: this.api_name + `/auth/findUserAddressList`,
            method: 'get'
        })
    },

    findBaseRegion() {
        return request({
            url: this.api_name + `/auth/findBaseRegion`,
            method: 'get'
        })
    },

    findBaseProvinceByRegionId(regionId) {
        return request({
            url: this.api_name + `/auth/findBaseProvinceByRegionId/` + regionId,
            method: 'get'
        })
    }
}
