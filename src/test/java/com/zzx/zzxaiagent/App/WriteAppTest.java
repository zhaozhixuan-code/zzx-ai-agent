package com.zzx.zzxaiagent.App;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@Slf4j
class WriteAppTest {

    @Resource
    private WriteApp writeApp;
    private final String URL = "https://v2.xxapi.cn/api/detect?text=";

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        System.out.println(writeApp.doChat("你好,我叫赵志轩", chatId));
        System.out.println(writeApp.doChat("我叫什么来着？", chatId));
    }


    /**
     * 结构化输出
     */
    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String userPrompt = "你好,我叫赵志轩";
        System.out.println(writeApp.doChatWithReport(userPrompt, chatId));
        System.out.println(writeApp.doChatWithReport("我叫什么来着？", chatId));
    }

    /**
     * RAG
     */
    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        System.out.println(writeApp.doChatWithRag("你好，我有写作需求", chatId));
        // System.out.println(writeApp.doChatWithRag("日记总是坚持不下来，或者写成流水账，怎么办？", chatId));

    }
}