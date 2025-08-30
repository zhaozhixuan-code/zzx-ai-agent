package com.zzx.zzxaiagent.advisor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.UserMessage;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 违禁词校验拦截器
 */
@Slf4j
public class ProhibitedWordAdvisor implements CallAdvisor, StreamAdvisor {

    private final String URL = "https://v2.xxapi.cn/api/detect?text=";


    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 1;
    }

    /**
     * 检查文本中是否包含违规词
     *
     * @param text 输入文本
     * @return 是否有违规词 true 有   false 无
     */
    private boolean checkProhibited(String text) {
        // 发送 GET 请求
        String url = URL + text;
        try (HttpResponse response = HttpRequest.get(url)
                .header("User-Agent", "xiaoxiaoapi/1.0.0 (https://xxapi.cn )")
                .execute()) {
            // 检查HTTP状态码
            if (!response.isOk()) {
                log.warn("违规词检测服务异常，HTTP状态码: {}", response.getStatus());
                // 违规
                return true;
            }
            // 获取响应体
            JSONObject data = JSONUtil.parseObj(response.body()).getJSONObject("data");
            // log.info(String.valueOf(data));
            // 获取检测结果状态
            String status = data.getStr("status", "error");
            if (status.equals("error")) {
                // 检测失败，返回
                return true;
            }
            // 获取是否为违规
            boolean isProhibited = data.getBool("is_prohibited");
            return isProhibited;
        } catch (Exception e) {
            log.error("违规词检测请求失败: URL={}, 错误: {}", url, e.getMessage(), e);
            return true; // 检测失败时保守返回true
        }
    }

    private void handleProhibitedWords(String type, boolean isProhibited) {
        // 如果包含违规词
        if (BooleanUtil.isTrue(isProhibited)) {
            String message = String.format("检测到%s中包含违禁词", type);
            throw new IllegalArgumentException(message);
        }
    }

    private ChatClientRequest before(ChatClientRequest request) {
        UserMessage lastUserMessage = request.prompt().getUserMessage();
        // 最后一条用户消息内容
        String userText = lastUserMessage.getText();
        // 检查是否有违规词
        boolean isProhibited = checkProhibited(userText);
        handleProhibitedWords("用户请求", isProhibited);
        return request;
    }

    private void observeAfter(ChatClientResponse chatClientResponse) {
        String AIText = chatClientResponse.chatResponse().getResult().getOutput().getText();
        // 检查是否有违规词
        boolean isProhibited = checkProhibited(AIText);
        handleProhibitedWords("AI的回复", isProhibited);
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain chain) {
        chatClientRequest = before(chatClientRequest);
        ChatClientResponse chatClientResponse = chain.nextCall(chatClientRequest);
        observeAfter(chatClientResponse);
        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain chain) {
        chatClientRequest = before(chatClientRequest);
        Flux<ChatClientResponse> chatClientResponseFlux = chain.nextStream(chatClientRequest);
        return new ChatClientMessageAggregator().aggregateChatClientResponse(chatClientResponseFlux, this::observeAfter);
    }
}
