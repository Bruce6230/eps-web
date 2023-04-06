package com.makiyo.eps.api.pojo;

import lombok.Data;

@Data
public class ResultMessage {
    //是否系统消息
    private Boolean isSystem;
    //来自
    private String fromName;
    //消息内容
    private Object message;
}
