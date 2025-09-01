package com.zzx.zzxaiagent.App;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
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

    /**
     * 调用工具
     */
    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("推荐几个写作技巧");

        // 测试网页抓取
        testMessage("看一看bilibili.com,");
/*
        // 测试资源下载：图片下载
        testMessage("直接下载一张适合做手机壁纸的图片为文件");

        // 测试终端操作：执行代码
        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        testMessage("保存我的写作档案为文件");

        // 测试 PDF 生成
        testMessage("生成一份‘写作报告’PDF，包含写作内容"); */
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = writeApp.doChatWithTools(message, chatId);
        System.out.println(answer);
    }
}