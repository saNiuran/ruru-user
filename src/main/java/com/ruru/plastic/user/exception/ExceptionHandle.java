package com.ruru.plastic.user.exception;

import com.ruru.plastic.user.response.CodeResponse;
import com.ruru.plastic.user.response.DataResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandle {
    /**
     * 声明需要捕获的异常类 - 写成PeopleException，就是只会捕获PeopleException异常了
     */
    @ExceptionHandler(value = Exception.class)
    public DataResponse<Void> handle(Exception e){
        if(e instanceof CommonException){
            CommonException exception = (CommonException) e;
            return DataResponse.error(new CodeResponse(exception.getCode(), exception.getMessage()));
        }else if(e instanceof HttpRequestMethodNotSupportedException){
            HttpRequestMethodNotSupportedException exception = (HttpRequestMethodNotSupportedException) e;
            return DataResponse.error(new CodeResponse(500,exception.getMessage()));
        } else {
            e.printStackTrace();
            return DataResponse.error(new CodeResponse(500,"服务器异常"));
        }
    }
}
