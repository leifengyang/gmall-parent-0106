var login = {

  api_name : '/api/user/passport',

  register(userInfo) {
    return request({
      url: this.api_name + '/register',
      method: `post`,
      data: userInfo
    })
  },

  login(userInfo) {
    return request({
      url: this.api_name + '/login',
      method: 'post',
      data: userInfo
    })
  },

  logout() {
    return request({
      url: this.api_name + '/logout',
      method: 'get'
    })
  },

  sendCode(phone) {
    return request({
      url: this.api_name + '/sendCode/' + phone,
      method: 'get'
    })
  }
}
