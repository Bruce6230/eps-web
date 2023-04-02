package com.makiyo.eps.api.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@ResponseBody
@Slf4j
public class EpsException extends RuntimeException {
    private String msg;
    private int code = 500;

    public EpsException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public EpsException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public EpsException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public EpsException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e) {
        log.error("exceptionHandler", e);
        return ResponseEntity.status(500).body(e.getMessage());
    }

}