package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.gateway.properties.AuthProperties;
import com.atguigu.gmall.model.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 拦截所有请求
 * Webflux： 响应式编程；
 * 不是以前阻塞式编程；消息的监听者；异步运行并且把运行的结果放到消息队列（RabbitMQ），其他需要此次调用结果的人，监听队列即可
 * <p>
 * 【发布者】、负责发送【消息流】给【订阅者】。
 * 响应式系统完全不阻塞，返回的变量都是消息的发布者，想要看真正值了就去订阅。
 * 响应式调用任何方法会立即返回一个发布者，后台慢慢执行这个方法。需要结果就订阅这个发布者
 * <p>
 * 【数据流】万物皆是流。
 * Mono：0|1数据
 * Flux：N数据
 * <p>
 * <p>
 * 整个网站所有的请求分为以下几类：
 * 1、需要登录才能访问
 * 2、无论任何情况，浏览器都不能访问【集群内部的所有RPC路径】
 * 3、无论任何情况都可以访问;【直接放行。静态资源】
 */
@Slf4j
@Component
public class GlobalAuthFilter implements GlobalFilter {


    @Autowired
    AuthProperties authProperties;

    //ant风格的路径匹配器
    AntPathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * @param exchange request+response
     * @param chain
     * @return
     */
    @Override  //Void
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        log.info("拦截到请求[{}]", path);

        //判断需要拦截还是放行

        //1、/rpc/inner/** 所有请求都不能访问。响应Result.fail();
        if (pathMatch(authProperties.getNoAuthUrl(), path)) {
            //当前浏览器访问了非授权页 /rpc/inner/**
            //打回去，响应错误的json
            return writeJson(response, ResultCodeEnum.NOAUTH_URL);
        }

        //2、再看是否需要登录才能访问
        if (pathMatch(authProperties.getLoginUrl(), path)) {
            //3、验证登录的用户； 拿到令牌
            String token = getToken(request);
            UserInfo info = validToken(token);
            if (info != null) {
                //4、校验token 通过
                Long id = info.getId();
                //5、放行之前，需要给请求头中加一个 UserId 字段。
                //request 是不允许改的。我们需要创建一个新的
                ServerHttpRequest newReq = request.mutate()
                        .header("UserId", id.toString())
                        .build();

                ServerWebExchange newExchange = exchange.mutate()
                        .request(newReq)
                        .response(response)
                        .build();

                //6、放行
                return chain.filter(newExchange);
            } else {
                //5、校验token 不通过。 打回登录页，去登录
                log.info("用户令牌【{}】非法，打回登录页", token);
                return locationToPage(response, authProperties.getLoginPage());
            }
        }


        //3、既不是非法请求，又无需登录
        return chain.filter(exchange);
    }


    /**
     * 跳转到指定页面
     *
     * @param response
     * @param loginPage
     * @return
     */
    private Mono<Void> locationToPage(ServerHttpResponse response, String loginPage) {
        //1、给浏览器响应一个状态码 302。 响应头命令浏览器跳转一个位置  Location:  xjkjsalj
        // (2xx  3xx)失败  (4xx[客户端失败] 5xx[服务器完蛋])失败
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().set("Location", loginPage);
        return response.setComplete(); //响应结束
    }

    /**
     * 校验令牌
     *
     * @param token
     * @return
     */
    private UserInfo validToken(String token) {
        //1、没令牌
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        UserInfo info = getUserInfo(token);
        if (info == null) {
            //2、有令牌但是是假令牌
            return null;
        }

        return info;
    }

    /**
     * 根据令牌去redis检索用户信息
     *
     * @param token
     * @return
     */
    private UserInfo getUserInfo(String token) {
        String json = redisTemplate.opsForValue().get(RedisConst.USER_LOGIN_PREFIX + token);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        UserInfo info = JSONs.toObj(json, UserInfo.class);
        return info;
    }


    /**
     * 获取令牌
     *
     * @param request
     * @return
     */
    private String getToken(ServerHttpRequest request) {
        //1、先看Cookie有没有
        String token = request.getCookies().getFirst("token").getValue();
        if (StringUtils.isEmpty(token)) {
            //2、尝试去token头中取
            token = request.getHeaders().getFirst("token");
        }
        return token;
    }

    /**
     * 写出json
     *
     * @param response
     * @param codeEnum
     * @return
     */
    private Mono<Void> writeJson(ServerHttpResponse response, ResultCodeEnum codeEnum) {
        Result<String> result = Result.build("", codeEnum);
        //从response的buffer工厂，拿到响应体的databuffer
        DataBuffer wrap = response.bufferFactory().wrap(JSONs.toStr(result).getBytes(StandardCharsets.UTF_8));

        //指定字符编码
        response.getHeaders().add("content-type", "application/json;charset=utf-8");
        return response.writeWith(Mono.just(wrap));
    }


    /**
     * 路径匹配
     *
     * @param patterns
     * @param path
     * @return
     */
    private boolean pathMatch(List<String> patterns, String path) {
        long count = patterns.stream()
                .filter(pattern -> pathMatcher.match(pattern, path))
                .count();
        return count > 0;
    }
}
