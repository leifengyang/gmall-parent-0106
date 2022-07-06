package com.atguigu.gmall.common.retry;

import feign.RetryableException;
import feign.Retryer;

public class RequestNeverRetryer  implements Retryer {
    @Override
    public void continueOrPropagate(RetryableException e) {
        throw e; //只要重试器抛出了异常就会打断重试逻辑
    }

    @Override
    public Retryer clone() {
        return this;
    }
}
