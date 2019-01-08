package cn.wolfcode.p2p.controller;

import cn.wolfcode.p2p.base.exception.CustomException;
import cn.wolfcode.p2p.util.JsonResult;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
public class CommonExceptionHandler {

    /**
     * 处理LoginInfo相关的异常
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public JsonResult customExceptionHandler(CustomException e){
        return JsonResult.instance(e.getMessage());
    }


    /**
     * 处理LoginInfo的数据异常
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public JsonResult bindExceptionHandler(BindException e){
        List<ObjectError> errors = e.getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (ObjectError error : errors) {
            sb.append(error.getDefaultMessage()+",");
        }
        return JsonResult.instance(sb.toString());
    }

    /**
     * 处理其他的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult exceptionHandler(Exception e){
        e.printStackTrace();
        return JsonResult.instance(e.getMessage());
    }
}
