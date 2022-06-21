package com.atguigu.gmall.product.exception;


import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//@ResponseBody
//@ControllerAdvice

/**
 * 处理全局统一异常
 * 1、所有的业务异常都是一个异常 throw new GmallException(业务错误码);
 * 2、系统其它异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 业务异常就直接根据业务码响应错误
     * @param e
     * @return
     */
    @ExceptionHandler(GmallException.class)
    public Result handleBizException(GmallException e){
        Result<String> result = new Result<String>();
        result.setCode(e.getCode());
        result.setMessage(e.getMessage());
//        String errorJson = JSON.toJSONString(e.getStackTrace());
        result.setData("");
        return result;
    }


    /**
     * 系统其它异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleOtherException(Exception e){
        Result<Object> fail = Result.fail();
        fail.setMessage(e.getMessage());
        return fail;
    }



}
