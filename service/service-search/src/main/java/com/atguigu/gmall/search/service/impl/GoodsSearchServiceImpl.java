package com.atguigu.gmall.search.service.impl;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.*;
import com.atguigu.gmall.search.repo.GoodsRepositry;
import com.atguigu.gmall.search.service.GoodsSearchService;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GoodsSearchServiceImpl implements GoodsSearchService {

    @Autowired
    GoodsRepositry goodsRepositry;

    @Autowired
    ElasticsearchRestTemplate esTemplate;

    @Override
    public void upGoods(Goods goods) {
        goodsRepositry.save(goods);
    }

    @Override
    public void downGoods(Long skuId) {
        goodsRepositry.deleteById(skuId);
    }


    //浏览器点击按钮---检索---网关（filter）---web-front---远程调用search----(收到参数)检索服务[进行检索](返回结果)---浏览器
    //检索
    @Override
    public SearchResponseVo search(SearchParam param) {

        //SearchParam param;//前端检索传来的所有参数
        //TODO 真正检索
        //1、根据前端传来的检索
        Query query = buildQuery(param);
        /**
         * Query query：            查询条件
         * Class<T> clazz：         查到的数据封装成什么
         * IndexCoordinates index： 去哪个索引查
         */
        //2、检索：得到命中的记录
        SearchHits<Goods> goods = esTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));

        //3、把检索的结果封装成前端能用的对象
        SearchResponseVo vo = buildResponse(goods, param);

        return vo;
    }

    //3、把检索的结果封装成前端能用的对象
    private SearchResponseVo buildResponse(SearchHits<Goods> result, SearchParam param) {
        SearchResponseVo vo = new SearchResponseVo();

        //1、拿到所有查到的商品
        List<Goods> list = new ArrayList<>();
        for (SearchHit<Goods> hit : result.getSearchHits()) {
            Goods good = hit.getContent();
            list.add(good);
        }
        vo.setGoodsList(list);

        //2、当前页面，总页数
        vo.setPageNo(param.getPageNo());
        //总记录数
        long hits = result.getTotalHits();
        //全系统pageSize=10
        long l = hits % 10 == 0 ? hits / 10 : (hits / 10 + 1);
        vo.setTotalPages(new Integer(l + ""));


        //3、检索条件
        vo.setSearchParam(param);


        //4、品牌列表
        List<TrademarkSearchVo> tmList = new ArrayList<>();
        //拿到所有聚合结果
        ParsedLongTerms tmIdAgg = result.getAggregations().get("tmIdAgg");
        //遍历品牌id聚合结果的桶
        for (Terms.Bucket bucket : tmIdAgg.getBuckets()) {
            //4.1、拿到品牌id
            long tmId = bucket.getKeyAsNumber().longValue();
            //4.2、拿到品牌名字
            ParsedStringTerms tmNameAgg = bucket.getAggregations().get("tmNameAgg");
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            //4.3、拿到品牌名字
            ParsedStringTerms tmLogoAgg = bucket.getAggregations().get("tmLogoAgg");
            String tmLogo = tmLogoAgg.getBuckets().get(0).getKeyAsString();


            //封装vo
            TrademarkSearchVo tmVo = new TrademarkSearchVo();
            tmVo.setTmId(tmId);
            tmVo.setTmName(tmName);
            tmVo.setTmLogoUrl(tmLogo);
            tmList.add(tmVo);

        }
        vo.setTrademarkList(tmList);


        //5、属性列表（属性的进阶检索）：分析聚合结果
        List<AttrSearchVo> attrsList = new ArrayList<>();
        ParsedNested attrAgg = result.getAggregations().get("attrAgg");
        //拿到attrId的值分布
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");
        //StreamAPI
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            //5.1、属性id
            long attrId = bucket.getKeyAsNumber().longValue();
            //5.2、属性名
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            //5.3、属性值
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attrValueAgg");
            List<String> vals = new ArrayList<>();
            for (Terms.Bucket valueAggBucket : attrValueAgg.getBuckets()) {
                String attrValue = valueAggBucket.getKeyAsString();
                vals.add(attrValue);
            }
            //封装vo
            AttrSearchVo attrVo = new AttrSearchVo();
            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValueList(vals);
            attrsList.add(attrVo);

        }
        vo.setAttrsList(attrsList);




        //前端的当时检索时的完整请求路径
        String urlParam = makeUrlParam(param);
        vo.setUrlParam(urlParam);


        //1:小米；制作品牌面包屑
        String trademark = param.getTrademark();
        if(!StringUtils.isEmpty(trademark)){
            vo.setTrademarkParam("品牌："+trademark.split(":")[1]);
        }

        //属性的面包屑
        if(param.getProps()!=null && param.getProps().length>0){
            String[] props = param.getProps();
            //3:6GB:运行内存   4:64GB:机身存储
            //制作每个检索属性的面包屑
            List<AttrBread> breads = Arrays.stream(props).map(str -> {
                String[] split = str.split(":");
                AttrBread bread = new AttrBread();
                bread.setAttrId(Long.parseLong(split[0]));
                bread.setAttrValue(split[1]);
                bread.setAttrName(split[2]);
                return bread;
            }).collect(Collectors.toList());

            vo.setPropsParamList(breads);
        }


        // order=2:desc
        //回显orderMap
        String order = param.getOrder(); //order=2:asc  order=1:desc
        OrderMap orderMap = new OrderMap();
        if(!StringUtils.isEmpty(order)){
            orderMap.setType(order.split(":")[0]);
            orderMap.setSort(order.split(":")[1]);
        }

        vo.setOrderMap(orderMap);

        return vo;
    }


    private String makeUrlParam(SearchParam param) {
        StringBuilder urlBuilder = new StringBuilder("/list.html?");
        if(param.getPageNo()!=null){
            // /list.html?pageNo=1
            urlBuilder.append("&pageNo="+param.getPageNo());
        }

        if(param.getCategory1Id()!=null){
            urlBuilder.append("&category1Id="+param.getCategory1Id());
        }
        if(param.getCategory2Id()!=null){
            urlBuilder.append("&category2Id="+param.getCategory2Id());
        }
        if(param.getCategory3Id()!=null){
            urlBuilder.append("&category3Id="+param.getCategory3Id());
        }
        if(!StringUtils.isEmpty(param.getKeyword())){
            urlBuilder.append("&keyword="+param.getKeyword());
        }
        if(!StringUtils.isEmpty(param.getTrademark())){
            urlBuilder.append("&trademark="+param.getTrademark());
        }
        if(param.getProps()!=null &&param.getProps().length>0 ){
            Arrays.stream(param.getProps()).forEach(prop->{
                urlBuilder.append("&props="+prop);
            });
        }
//        if(!StringUtils.isEmpty(param.getOrder())){
//            urlBuilder.append("&order="+param.getOrder());
//        }



        return urlBuilder.toString();
    }


    //DSL结构不确定，自己编码构造出Query
    //1、根据前端传来的检索条件构造处一个检索可以使用的Query对象
    private Query buildQuery(SearchParam param) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //1、总的query
        NativeSearchQuery dsl = new NativeSearchQuery(boolQuery);

        //========查询条件=========
        //2、bool
        //3、bool-must-分类的term
        if (param.getCategory3Id() != null) {
            //3.1、按照三级分类id查询
            boolQuery.must(QueryBuilders.termQuery("category3Id", param.getCategory3Id()));
        }
        if (param.getCategory2Id() != null) {
            //3.2、按照三级分类id查询
            boolQuery.must(QueryBuilders.termQuery("category2Id", param.getCategory2Id()));
        }
        if (param.getCategory1Id() != null) {
            //3.3、按照三级分类id查询
            boolQuery.must(QueryBuilders.termQuery("category1Id", param.getCategory1Id()));
        }

        //4、bool-must- 商品名字的 match
        if (!StringUtils.isEmpty(param.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("title", param.getKeyword()));
        }

        //5、bool-must- 品牌的精确匹配
        if (!StringUtils.isEmpty(param.getTrademark())) {
            String trademark = param.getTrademark(); //1:小米
            String[] split = trademark.split(":");
            boolQuery.must(QueryBuilders.termQuery("tmId", split[0]));
        }
        //6、bool-must- 按照属性的精确匹配
        if (param.getProps() != null && param.getProps().length > 0) {
            for (String prop : param.getProps()) {
                //["3:6GB:运行内存","4:64GB:机身存储"]
                String[] split = prop.split(":");
                //nested里面的boolQuery
                BoolQueryBuilder propBool = QueryBuilders.boolQuery();
                propBool.must(QueryBuilders.termQuery("attrs.attrId", split[0]));
                propBool.must(QueryBuilders.termQuery("attrs.attrValue", split[1]));

                boolQuery.must(QueryBuilders.nestedQuery("attrs", propBool, ScoreMode.None));
            }
        }


        //=====排序条件======
        if (!StringUtils.isEmpty(param.getOrder())) {
            //2:asc
            Sort sort = null;
            String[] split = param.getOrder().split(":");
            switch (split[0]) {
                case "1":
                    sort = split[1].equalsIgnoreCase("asc") ? Sort.by("hotScore").ascending() : Sort.by("hotScore").descending();
                    break;
                case "2":
                    sort = split[1].equalsIgnoreCase("asc") ? Sort.by("price").ascending() : Sort.by("price").descending();
                    break;
            }
            dsl.addSort(sort);
        }

        //====分页条件====
        if (param.getPageNo() != null) {
            //页码从0开启
            PageRequest request = PageRequest.of(param.getPageNo().intValue() - 1, 10);
            dsl.setPageable(request);
        }

        //====聚合分析====
        //==分析：品牌==
        //1、品牌id-聚合
        TermsAggregationBuilder tmIdAgg = AggregationBuilders
                .terms("tmIdAgg")
                .field("tmId")
                .size(100);

        //2、品牌id-聚合- 品牌名子聚合
        TermsAggregationBuilder tmNameSubAgg = AggregationBuilders.terms("tmNameAgg")
                .field("tmName")
                .size(1);
        tmIdAgg.subAggregation(tmNameSubAgg);
        //2、品牌id-聚合- 品牌logo子聚合
        TermsAggregationBuilder tmLogoSubAgg = AggregationBuilders.terms("tmLogoAgg")
                .field("tmLogoUrl")
                .size(1);
        tmIdAgg.subAggregation(tmLogoSubAgg);

        dsl.addAggregation(tmIdAgg);


        ////==分析：平台属性==
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");

        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg")
                .field("attrs.attrId")
                .size(100);

        //attrNameAgg
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1));

        //attrValueAgg
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(100));
        attrAgg.subAggregation(attrIdAgg);
        dsl.addAggregation(attrAgg);

        return dsl;
    }
}
