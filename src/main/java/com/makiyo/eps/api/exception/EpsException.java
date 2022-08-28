package com.makiyo.eps.api.exception;

import lombok.Data;

@Data
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

}