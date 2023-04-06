package com.makiyo.eps.api.utils.api.res.chat.text;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChatChoice implements Serializable {
    private long index;
    @JsonProperty("delta")
    private Message delta;
    @JsonProperty("message")
    private Message message;
    @JsonProperty("finish_reason")
    private String finishReason;
}
