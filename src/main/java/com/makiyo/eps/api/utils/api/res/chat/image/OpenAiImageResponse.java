package com.makiyo.eps.api.utils.api.res.chat.image;

import lombok.Data;

import java.util.List;

@Data
public class OpenAiImageResponse {
    private Long created;
    private List<DataRes> data;
}
