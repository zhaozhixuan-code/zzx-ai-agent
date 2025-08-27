package com.zzx.zzxaiagent.App;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WriteApp {

/*     private final ChatClient chatClient;

    public WriteApp(ChatModel dashscopeChatModel) {
        // 加载系统prompt
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(new ClassPathResource("prompts/system-message.st"));


        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem().build();
    } */
}
