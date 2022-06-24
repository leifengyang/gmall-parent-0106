var hm = {

    /**
     * 默认配置
     */
    default: {
        // log采集地址（需配置）
        // sendUrl: "http://47.93.148.192:7070/log.gif",
        sendUrl: "https://gmall.goho.co/applog",
        // sendUrl: "http://gmalllog.viphk.ngrok.org/applog",

        // 曝光埋点的自定义DOM元素class名称
        displayClass: "hm-display-statistics",
        // 曝光埋点的DOM元素的属性
        displayAttr: "hm-display",


        // 行为埋点的自定义DOM元素class名称
        actionClass: "hm-action-statistics",
        // 行为埋点的DOM元素的属性
        actionAttr: "hm-action",
    },

    /**
     * 页面数据
     */
    data: {
        //初始化页面数据
        pageInitData: null,

        // 曝光的所有数据集合
        displaysData: [],

        // 事件行为的所有数据集合
        actionsData: []
    },

    /**
     * 公共信息
     */
    common: {
        mid: '',  // 设备id
        uid: '', // 用户唯一标识
        vc: '2.0',  //程序版本号
        ch: 'web', // 渠道号，应用从哪个渠道来的
        os: '', // 系统版本
        ar: '北京', // 区域
        md: navigator.userAgent, // 手机型号
        ba: navigator.appName, // 手机品牌
        is_new: '0'//是否新用户
    },
    // common: {
    //     browser: navigator.userAgent, // 浏览器信息
    //     title: encodeURIComponent(document.title), // 页面标题
    //     resolve: window.screen.width + 'x' + window.screen.height + '(px)', // 设备分辨率
    //     // deviceType: null, // 设备类型
    //     language: navigator.language, // 客户端语言
    //     mid: '',  // 设备id
    //     userId: '', // 用户唯一标识
    //     url: location.href, // 页面链接
    //     referrer: document.referrer || ''//当前页面的来源页面的URL
    // },

    /**
     * 进入页面初始化
     */
    init: function () {
        let that = this

        // 初始化设备id
        that.common.mid = that.util.getMid()
        // 初始化用户id，当前登录用户id
        that.common.uid = auth.getUserInfo() != null ? auth.getUserInfo().userId : ''
        that.common.os = that.util.getOsInfo()
        that.common.is_new = that.util.getIsNew()

        // 记录初始化页面信息
        that.data.pageInitData = {
            prePath: that.getPrePath(), // 获取上一个页面id
            time: new Date().getTime() // 获取进入页面的时间
        };

        // 用户事件行为埋点监控，由于部分页面使用vue异步渲染，由于页面加载先后问题，保证监控内容被完全监控，所以延迟监控
        setTimeout('hm.actionStatistics()',1000);

        // 用户可视化区域展示埋点监控，由于部分页面使用vue异步渲染，由于页面加载先后问题，保证监控内容被完全监控，所以延迟监控
        setTimeout('hm.displayStatistics()',1000);
    },

    /**
     * 获取上一个页面id
     * @returns {*|string}
     */
    getPrePath: function() {
        let that = this
        var prePath = that.util.getCookie('prePath')
        if(prePath == '' || prePath == 'undefined') {
            prePath = ""
        }
        // 记录当前页面id
        that.util.setCookie('prePath', window.page.page_id, 1*60*1000)
        return prePath
    },

    /**
     * 用户可视化区域展示埋点监控
     */
    displayStatistics: function () {
        let that = this
        let observer = new IntersectionObserver(function (entries) {
            entries.forEach((entry, index) => {
                // 这段逻辑，是每一个被观察的组件进入视窗时都会触发的
                if (entry.isIntersecting) {
                    // 把进入视口的组件数据添加进待上报的数据对象中
                    that.data.displaysData.push(JSON.parse(entry.target.attributes[that.default.displayAttr].value.replaceAll("'", "\"")))
                    // 停止观察进入视口的组件
                    observer.unobserve(entry.target)
                }
            })
        }, {
            root: null, // 默认根节点是视口
            rootMargin: '0px',
            threshold: 1 // 全部进入视口才被观察  这个阈值介于0和1之间
        })

        var displayItem = document.querySelectorAll('.' + that.default.displayClass)
        displayItem.forEach(item => {
            observer.observe(item) // 观察每一个进入视口的区域
        })
    },

    /**
     * 用户事件行为埋点监控
     */
    actionStatistics: function () {
        let that = this
        $("." + that.default.actionClass).each(function(){
            $(this).click(function(){
                var action = JSON.parse($(this).attr(that.default.actionAttr).replaceAll("'", "\""))
                action.ts = new Date().getTime()

                that.data.actionsData.push(action)
            });
        });
    },

    /**
     * 页面信息
     */
    getPageData: function() {
        var that = this
        var page = {
            "page_id": window.page.page_id,
            "last_page_id": that.data.pageInitData.prePath,
            //"item": window.page.item,
            "item_type": window.page.page_item_type,
            "item": window.page.page_item,
            "sourceType": window.page.sourceType,
            "during_time": new Date().getTime() - that.data.pageInitData.time
        };
        return page
    },

    /**
     * 离开页面上报数据  r
     */
    postData: function () {
        //上报数据
        var data = {
            common: this.common,
            page: this.getPageData(),
            displays: this.data.displaysData,
            actions: this.data.actionsData,
            ts: this.data.pageInitData.time
        };

        // $.ajax({
        //     type: "POST",
        //     url: this.default.sendUrl+"?v="+Math.random(),
        //     contentType: "application/json; charset=utf-8",
        //     data: JSON.stringify(data),
        //     dataType: "json",
        //     success: function (message) {
        //         console.log('result:'+message);
        //
        //     },
        //     error: function (message) {
        //         console.log('error:'+message);
        //     }
        // });
        console.log(JSON.stringify(data))
        //图片上报
        new Image().src = this.default.sendUrl+"?param="+JSON.stringify(data)+"&v="+Math.random();
        new Image().src = "http://47.93.148.192:7070/log.gif?param="+JSON.stringify(data)+"&v="+Math.random();
    },

    /**
     * 工具函数
     */
    util: {
        setCookie: function (name, value, time) {
            var date = new Date()
            date.setTime(date.getTime() + time)
            $.cookie(name, encodeURIComponent(value), {domain: 'gmall.com', expires: date, path: '/'})
        },

        getCookie: function (name) {
            return decodeURIComponent($.cookie(name))
        },

        /**
         * 获取设备id
         * @returns {string|*|string}
         */
        getMid: function () {
            var mid = this.getCookie('MID');
            if(mid != '' && mid != 'undefined') {
                return mid;
            } else {
                var d = new Date().getTime();
                if (window.performance && typeof window.performance.now === "function") {
                    d += performance.now(); //use high-precision timer if available
                }
                var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    var r = (d + Math.random() * 16) % 16 | 0;
                    d = Math.floor(d / 16);
                    return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
                });
                this.setCookie('MID', uuid, 365*24*60*60*1000);
                return uuid;
            }
        },

        /**
         * 是否新用户
         * @returns {string|*|string}
         */
        getIsNew: function () {
            var initDate = this.getCookie('INIT_DATE');
            if(initDate != '' && initDate != 'undefined') {
                var nowDate = this.getNowFormatDate()
                if(initDate != nowDate) {
                    return "0";
                }
            } else {
                initDate = this.getNowFormatDate();
                this.setCookie('INIT_DATE', initDate, 365*24*60*60*1000);
            }
            return "1";
        },

        getNowFormatDate: function() {
            var date = new Date();
            var seperator1 = "-";
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            var strDate = date.getDate();
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            if (strDate >= 0 && strDate <= 9) {
                strDate = "0" + strDate;
            }
            var currentdate = year + seperator1 + month + seperator1 + strDate;
            return currentdate;
        },

        // 获取操作系统信息
        getOsInfo: function () {
            var userAgent = navigator.userAgent.toLowerCase();
            var name = 'Unknown';
            var version = 'Unknown';
            if (userAgent.indexOf('win') > -1) {
                name = 'Windows';
                if (userAgent.indexOf('windows nt 5.0') > -1) {
                    version = 'Windows 2000';
                } else if (userAgent.indexOf('windows nt 5.1') > -1 || userAgent.indexOf('windows nt 5.2') > -1) {
                    version = 'Windows XP';
                } else if (userAgent.indexOf('windows nt 6.0') > -1) {
                    version = 'Windows Vista';
                } else if (userAgent.indexOf('windows nt 6.1') > -1 || userAgent.indexOf('windows 7') > -1) {
                    version = 'Windows 7';
                } else if (userAgent.indexOf('windows nt 6.2') > -1 || userAgent.indexOf('windows 8') > -1) {
                    version = 'Windows 8';
                } else if (userAgent.indexOf('windows nt 6.3') > -1) {
                    version = 'Windows 8.1';
                } else if (userAgent.indexOf('windows nt 6.2') > -1 || userAgent.indexOf('windows nt 10.0') > -1) {
                    version = 'Windows 10';
                } else {
                    version = 'Unknown';
                }
            } else if (userAgent.indexOf('iphone') > -1) {
                name = 'Iphone';
            } else if (userAgent.indexOf('mac') > -1) {
                name = 'Mac';
            } else if (userAgent.indexOf('x11') > -1 || userAgent.indexOf('unix') > -1 || userAgent.indexOf('sunname') > -1 || userAgent.indexOf('bsd') > -1) {
                name = 'Unix';
            } else if (userAgent.indexOf('linux') > -1) {
                if (userAgent.indexOf('android') > -1) {
                    name = 'Android';
                } else {
                    name = 'Linux';
                }
            } else {
                name = 'Unknown';
            }
            return version;
        }
    }
};

(function(){
    //进入页面初始化
    hm.init();

    // window.isRun = 0
    // //离开页面上报数据
    // window.onbeforeunload = function () {
    //     if(window.isRun == 0) {
    //         window.isRun = 1
    //         hm.postData();
    //     }
    // }
    //
    // //离开页面上报数据
    // window.onpagehide=function(){
    //     if(window.isRun == 0) {
    //         window.isRun = 1
    //         hm.postData();
    //     }
    // };
})();

