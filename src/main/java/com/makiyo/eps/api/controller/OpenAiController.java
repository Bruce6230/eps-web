package com.makiyo.eps.api.controller;

import com.makiyo.eps.api.pojo.MessageType;
import com.makiyo.eps.api.service.UserChatService;
import com.makiyo.eps.api.service.dto.Message;
import com.makiyo.eps.api.utils.api.OpenAiWebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping({"/openai"})
@RequiredArgsConstructor
public class OpenAiController {
    private final UserChatService userChatService;
    private final OpenAiWebClient openAiWebClient;


    /**
     * 发信息
     *
     * @param prompt 提示词
     * @param user   用户
     * @return
     */
    @GetMapping(value = "/completions/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamCompletions(String prompt, String user) {
        Assert.hasLength(user, "user不能为空");
        Assert.hasLength(prompt, "prompt不能为空");
        return userChatService.send(MessageType.TEXT, prompt, user);
    }

    /**
     * 内容检测
     * @param content
     * @return
     */
    @GetMapping("/checkContent")
    public Mono<ServerResponse> checkContent(@RequestParam String content) {
        log.info("req:{}", content);
        return openAiWebClient.checkContent(content);
    }

    /**
     * 获取历史记录
     *
     * @param user
     */
    @GetMapping(value = "/history")
    public Mono<List<Message>> history(String user) {
        Assert.hasLength(user, "user不能为空");
        return Mono.just(userChatService.getHistory(user));
    }

}
