package com.atguigu.gmall.search;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


/**
 * 1、es的自动配置
 * ElasticsearchRestClientAutoConfiguration： es-rest客户端配置
 *      能配置的所有属性：ElasticsearchRestClientProperties
 *      额外导入：
 *          ElasticsearchRestClientConfigurations.RestClientBuilderConfiguration.class,
 * 		    ElasticsearchRestClientConfigurations.RestHighLevelClientConfiguration.class,
 * 		    ElasticsearchRestClientConfigurations.RestClientFallbackConfiguration.class
 * 		      给容器中放了：
 * 		      RestClient（给es发送rest的客户端）
 * 		      RestHighLevelClient（es官方提供的操作es发送rest请求进行crud的高级客户端）
 *
 *
 *
 * ElasticsearchDataAutoConfiguration
 *          给容器中放了 ElasticsearchRestTemplate；
 *          xxxTemplate：操作第三方的工具了；RedisTemplate，JdbcTemplate，MongoTemplate，RabbitTemplate，ElasticsearchRestTemplate
 *
 *
 * ElasticsearchRepositoriesAutoConfiguration:
 *         如果我们有一个注解 @EnableElasticsearchRepositories  【开启es仓库功能】；
 *                         开启自动扫描系统中标了 @Document（Person 这是es中要保存的一个文档） 的组件； @TableName
 *                         es启动就会给这个Person自动建立好索引。
 *
 *         ES： 索引 Index、 类Type、映射Mapping（自动决定）、文档Document、属性Field
 *      MySQL： 库Database、表Table、表结构TableStruct 、记录Record  、列column
 *
 *
 * 总结：
 * 1、自动注入 ElasticsearchRestTemplate ，就能对es进行复杂的操作
 * 2、简单的CRUD用ES的自动仓库功能；
 *      1）、@EnableElasticsearchRepositories 开启自动仓库功能
 *      2）、写一个repositry接口准备操作目标仓库
 *      3）、PersonRepositry 标注 @Repository 注解即可
 *      4）、
 *
 *
 *
 */
@EnableElasticsearchRepositories
@SpringCloudApplication
public class SearchMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchMainApplication.class,args);
    }
}
