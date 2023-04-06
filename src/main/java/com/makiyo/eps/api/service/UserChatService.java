package com.makiyo.eps.api.service;

import com.makiyo.eps.api.pojo.MessageType;
import com.makiyo.eps.api.service.dto.Message;
import reactor.core.publisher.Flux;

import java.util.List;

public interface UserChatService extends CompletedCallBack {

    /**
     * 发送消息
     *
     * @param type
     * @param content
     * @param sessionId
     */
    Flux<String> send(MessageType type, String content, String sessionId);

    /**
     * 消息历史
     *
     * @param sessionId
     * @return
     */
    List<Message> getHistory(String sessionId);


}
