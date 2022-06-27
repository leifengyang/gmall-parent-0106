package com.atguigu.gmall.product.schedule;


import com.atguigu.gmall.product.service.BloomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SkuBloomRebuildSchedule {

    @Autowired
    BloomService bloomService;
    //定时任务
    // *秒 *分 *时 *日 *月 *周
    // eg:  * 8 7 6 5 4；
    // 0 0 3 */7 * ?
//
//    @Scheduled(cron = "*/7 * * * * ?")
    @Scheduled(cron = "0 0 3 */7 * ?")
    public void rebuild(){
        bloomService.rebuildSkuBloom();
    }
}
