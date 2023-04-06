package com.makiyo.eps.api.service.dto;

import com.makiyo.eps.api.pojo.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRes {
    private MessageType messageType;
    private String message;
    private Boolean end;

}
