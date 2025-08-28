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
        String userPrompt = "你好";
        String chatId = UUID.randomUUID().toString();
        String s = writeApp.doChat(userPrompt, chatId);
        System.out.println(s);


    }



}