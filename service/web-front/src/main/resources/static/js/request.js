
var request = axios.create({
    baseURL:'http://api.gmall.com',
    timeout:100000
});

//添加一个请求拦截器
request.interceptors.request.use(function(config){
    //在请求发出之前进行一些操作
    //debugger
    if(auth.getToken()) {
        config.headers['token'] = auth.getToken();
    }
    if(auth.getUserTempId()) {
        config.headers['userTempId'] = auth.getUserTempId();
    }
    return config;
},function(error){
    //Do something with request error
    return Promise.reject(error);
});
//添加一个响应拦截器
request.interceptors.response.use(function(response){
    //在这里对返回的数据进行处理
    // debugger
    console.log(JSON.stringify(response))

    if (response.data.code == 208) {
        window.location.href = 'http://passport.gmall.com/login.html?originUrl='+window.location.href
    } else {
        // debugger
        if (response.data.code == 200) {
            return response
        } else {
            //秒杀业务与支付中业务
            if ((response.data.code >= 210 && response.data.code < 220) || response.data.code == 205) {
                return response
            } else {
                console.log("response.data:" + JSON.stringify(response.data))
                alert(response.data.message || 'error')
                return Promise.reject(response)
            }
        }
    }
},function(error){
    //Do something with response error
    return Promise.reject(error);
})
