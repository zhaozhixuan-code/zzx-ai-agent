package com.zzx.zzxaiagent.controller;

import com.zzx.zzxaiagent.App.WriteApp;
import com.zzx.zzxaiagent.agent.ZzxManus;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {

    // 写作大师应用
    @Resource
    private WriteApp writeApp;

    // 超级智能体
    @Resource
    private ZzxManus zzxManus;

    @Resource
    private ChatModel dashscopeChatModel;


    /**
     * 同步调用 AI 写作大师应用
     *
     * @param message 用户的信息
     * @param chatId  对话id
     * @return AI 的返回
     */
    @GetMapping("/write_app/chat/sync")
    public String doChatWithWriteAppSync(String message, String chatId) {
        String result = writeApp.doChat(message, chatId);
        return result;
    }


    /**
     * SSE 流式调用 AI 写作大师
     *
     * @param message 用户的信息
     * @param chatId  对话id
     * @return AI 的返回
     */
    @GetMapping(value = "/write_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithWriteAppSSE(String message, String chatId) {

        Flux<String> stringFlux = writeApp.doChatByStream(message, chatId);

        return stringFlux;
    }

    @GetMapping("/write_app/chat/sse/server_sent_event")
    public Flux<ServerSentEvent<String>> doChatWithWriteAppSSEServerSentEvent(String message, String chatId) {
        Flux<ServerSentEvent<String>> sentEventFlux = writeApp.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
        return sentEventFlux;
    }

    @GetMapping("write_app/chat/sse/emitter")
    public SseEmitter doChatWithWriteAppSseEmitter(String message, String chatId) {
        // 指定超时时间
        SseEmitter sseEmitter = new SseEmitter(100 * 60 * 5L);
        writeApp.doChatByStream(message, chatId)
                .subscribe(chunk -> {
                            try {
                                sseEmitter.send(chunk);
                            } catch (IOException e) {
                                sseEmitter.completeWithError(e);
                            }
                        },
                        // 处理错误
                        sseEmitter::completeWithError,
                        // 处理完成
                        sseEmitter::complete);
        return sseEmitter;
    }


}
