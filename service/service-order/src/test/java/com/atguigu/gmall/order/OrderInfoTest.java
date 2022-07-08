package com.atguigu.gmall.order;

import com.atguigu.gmall.model.activity.CouponInfo;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.order.OrderStatusLog;
import com.atguigu.gmall.model.payment.PaymentInfo;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.order.service.OrderStatusLogService;
import com.atguigu.gmall.order.service.PaymentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
public class OrderInfoTest {
    @Autowired
    OrderInfoService orderInfoService;
    @Autowired
    OrderDetailService orderDetailServicel;

    @Autowired
    PaymentInfoService paymentInfoService;

    @Autowired
    OrderStatusLogService orderStatusLog;




    /**
     * 同一个用户id。
     * 订单和订单的明细都会在同一个库中的各自表中；
     * 跨库无法join；为了避免出现跨库join；
     * 1）、订单和订单的明细【分库分片算法】规则一样。
     * 2）、绑定表；（不用写）
     *
     *
     *
     */
    @Test
    void testOtherSave(){
//        orderdetailsave();
//        paymentsave();
        OrderStatusLog statusLog = new OrderStatusLog();
        statusLog.setOrderId(1111L);
        statusLog.setUserId(16L);
        statusLog.setOrderStatus("dddd");
        statusLog.setOperateTime(new Date());

        orderStatusLog.save(statusLog);
    }

    private void paymentsave() {
        PaymentInfo info = new PaymentInfo();
        info.setOutTradeNo("324324321432");
        info.setOrderId(1110L);
        info.setPaymentType("d");
        info.setTradeNo("fasfdsafdas");
        info.setTotalAmount(new BigDecimal("999"));
        info.setSubject("fasfdsafdsa");
        info.setPaymentStatus("fdsafdsa");
        info.setCreateTime(new Date());
        info.setCallbackTime(new Date());
        info.setCallbackContent("fdsafdsfdsa");
        info.setUserId(16L);

        paymentInfoService.save(info);
    }

    private void orderdetailsave() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(213113L);
        orderDetail.setSkuId(11L);
        orderDetail.setSkuName("发送范德萨");
        orderDetail.setImgUrl("发送范德萨");
        orderDetail.setOrderPrice(new BigDecimal("333"));
        orderDetail.setSkuNum(3);
        orderDetail.setHasStock("1");
        orderDetail.setCreateTime(new Date());
        orderDetail.setSplitTotalAmount(new BigDecimal("333"));
        orderDetail.setSplitActivityAmount(new BigDecimal("333"));
        orderDetail.setSplitCouponAmount(new BigDecimal("333"));
        orderDetail.setUserId(16L);

        orderDetailServicel.save(orderDetail);
        System.out.println("orderDetail:"+orderDetail);
    }


    @Test
    void testQuery(){
        QueryWrapper<OrderInfo>  info = new QueryWrapper<>();
//        info.eq("user_id",17L);
        info.eq("id",249);
        orderInfoService.list(info).stream().forEach(item->{
            System.out.println("查到："+item);
        });

        //雪花算法

    }

    @Test
    void testsharding(){
        OrderInfo info = new OrderInfo();
        info.setConsignee("adadasd");
        info.setConsigneeTel("231231");
        info.setTotalAmount(new BigDecimal("777"));
        info.setOrderStatus("1");
        info.setUserId(16L);
        info.setPaymentWay("aa");
        info.setDeliveryAddress("aaa");
        info.setOrderComment("ddd");
        info.setOutTradeNo("fdfsafdsa");
        info.setTradeBody("fdasfdasfdsa");
        info.setCreateTime(new Date());
        info.setExpireTime(new Date());
        info.setProcessStatus("ddd");
        info.setTrackingNo("fffff");
        info.setParentOrderId(0L);
        info.setImgUrl("ggggg");
        info.setOrderDetailList(Lists.newArrayList());
        info.setWareId("11");
        info.setProvinceId(0L);
        info.setActivityReduceAmount(new BigDecimal("0"));
        info.setCouponAmount(new BigDecimal("0"));
        info.setOriginalTotalAmount(new BigDecimal("0"));
        info.setRefundableTime(new Date());
        info.setFeightFee(new BigDecimal("0"));
        info.setOperateTime(new Date());
        info.setOrderDetailVoList(Lists.newArrayList());
        info.setCouponInfo(new CouponInfo());

        orderInfoService.save(info);

        System.out.println("保存成功：订单id："+info.getId());
    }

    @Test
    void  testreadwritesplite(){

    }
}
