var auth = {

    getToken() {
        return $.cookie('token')
    },

    setToken(token) {
        return $.cookie('token', token, {domain: 'gmall.com', expires: 7, path: '/'})
    },

    removeToken() {
        return $.cookie('token', '', {domain: 'gmall.com', expires: 7, path: '/'})
    },

    isTokenExist() {
        return $.cookie('token') && $.cookie('token') != ''
    },

    getUserTempId() {
        return $.cookie('userTempId')
    },

    setUserTempId() {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "";

        var uuid = s.join("")
        return $.cookie('userTempId', uuid, {domain: 'gmall.com', expires: 365, path: '/'})
    },

    removeUserTempId() {
        return $.cookie('userTempId', '', {domain: 'gmall.com', expires: 7, path: '/'})
    },

    isUserTempIdExist() {
        return $.cookie('userTempId') && $.cookie('userTempId') != ''
    },

    getUserInfo() {
        if ($.cookie('userInfo')) {
            return JSON.parse($.cookie('userInfo'))
        }
        return null
    },

    setUserInfo(userInfo) {
        return $.cookie('userInfo', userInfo, {domain: 'gmall.com', expires: 7, path: '/'})
    },

    removeUserInfo() {
        return $.cookie('userInfo', '', {domain: 'gmall.com', expires: 7, path: '/'})
    },

    isUserInfoExist() {
        return $.cookie('userInfo') && $.cookie('userInfo') != ''
    },

    isLogin() {
        return this.isTokenExist()
    },

    getCookie(name) {
        return $.cookie(name)
    },

    setCookie(name, value) {
        return $.cookie(name, value, {domain: 'gmall.com', expires: 7, path: '/'})
    },

    removeCookie(name) {
        return $.cookie(name, '', {domain: 'gmall.com', expires: 7, path: '/'})
    },

    isExist(name) {
        return $.cookie(name) && $.cookie(name) != ''
    }
}



