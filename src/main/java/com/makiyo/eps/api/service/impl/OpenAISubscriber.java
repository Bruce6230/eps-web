package com.makiyo.eps.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.makiyo.eps.api.pojo.MessageType;
import com.makiyo.eps.api.service.CompletedCallBack;
import com.makiyo.eps.api.service.dto.Message;
import com.makiyo.eps.api.service.dto.MessageRes;
import com.makiyo.eps.api.utils.api.res.chat.image.DataRes;
import com.makiyo.eps.api.utils.api.res.chat.image.OpenAiImageResponse;
import com.makiyo.eps.api.utils.api.res.chat.text.OpenAiResponse;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.FluxSink;

import java.util.stream.Collectors;

@Slf4j
public class OpenAISubscriber implements Subscriber<String>, Disposable {
    private final FluxSink<String> emitter;
    private Subscription subscription;
    private final String sessionId;
    private final CompletedCallBack completedCallBack;
    private final StringBuilder sb;
    private final Message questions;
    private final MessageType messageType;

    public OpenAISubscriber(FluxSink<String> emitter, String sessionId, CompletedCallBack completedCallBack, Message questions) {
        this.emitter = emitter;
        this.sessionId = sessionId;
        this.completedCallBack = completedCallBack;
        this.questions = questions;
        this.sb = new StringBuilder();
        this.messageType = questions.getMessageType();
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(String data) {
        log.info("OpenAI返回数据：{}", data);
        if (messageType == MessageType.IMAGE) {
            subscription.request(1);
            sb.append(data);
            return;
        }
        if ("[DONE]".equals(data)) {
            log.info("OpenAI返回数据结束了");
            subscription.request(1);
            emitter.next(JSON.toJSONString(new MessageRes(MessageType.TEXT, "", true)));
            completedCallBack.completed(questions, sessionId, sb.toString());
            emitter.complete();
        } else {
            OpenAiResponse openAiResponse = JSON.parseObject(data, OpenAiResponse.class);
            String content = openAiResponse.getChoices().get(0).getDelta().getContent();
            content = content == null ? "" : content;
            emitter.next(JSON.toJSONString(new MessageRes(MessageType.TEXT, content, null)));
            sb.append(content);
            subscription.request(1);
        }

    }

    @Override
    public void onError(Throwable t) {
        log.error("OpenAI返回数据异常：{}", t.getMessage());
        emitter.error(t);
        completedCallBack.fail(sessionId);
    }

    @Override
    public void onComplete() {
        log.info("OpenAI返回数据完成");
        if (messageType == MessageType.IMAGE) {
            OpenAiImageResponse aiImageResponse = JSON.parseObject(sb.toString(), OpenAiImageResponse.class);
            String url = aiImageResponse.getData().stream().map(DataRes::getUrl).collect(Collectors.joining(","));
            emitter.next(JSON.toJSONString(new MessageRes(MessageType.IMAGE, url, true)));
        }
        emitter.complete();
    }

    @Override
    public void dispose() {
        log.warn("OpenAI返回数据关闭");
        emitter.complete();
    }
}