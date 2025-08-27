package com.zzx.zzxaiagent.demo;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// @Component
public class SpringAi implements CommandLineRunner {

    @Resource
    ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage message = dashscopeChatModel.call(new Prompt("你好"))
                .getResult()
                .getOutput();

        System.out.println(message);

    }
}
