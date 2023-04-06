package com.makiyo.eps.api.utils.api;

public interface ApiConstant {


    String HOST = "https://api.openai.com";

    String CHAT_API = HOST + "/v1/chat/completions";

    String CONTENT_AUDIT = HOST + "/v1/moderations";

    String IMAGE_API = HOST + "/v1/images/generations";
}
