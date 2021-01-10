package com.febs.web.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.febs.common.domain.ResponseBo;
import com.febs.common.exception.FileDownloadException;
import com.febs.common.exception.LimitAccessException;

@ControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = LimitAccessException.class)
    @ResponseBody
    public ResponseBo handleLimitAccessException(LimitAccessException e) {
        return ResponseBo.overClocking(e.getMessage());
    }

    @ExceptionHandler(value = FileDownloadException.class)
    @ResponseBody
    public ResponseBo handleFileDownloadException(LimitAccessException e) {
        return ResponseBo.error(e.getMessage());
    }

}
