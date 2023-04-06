package com.makiyo.eps.api.pojo;

import lombok.Data;

@Data
public class ChatCompletionsReq {
    private String prompt;
    private Integer maxTokens;
    private Double temperature;
    private Double topP;
    private String user;

}
