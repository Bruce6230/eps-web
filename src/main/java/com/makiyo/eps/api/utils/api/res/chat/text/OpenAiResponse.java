package com.makiyo.eps.api.utils.api.res.chat.text;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OpenAiResponse implements Serializable {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoice> choices;
    private Usage usage;
}
