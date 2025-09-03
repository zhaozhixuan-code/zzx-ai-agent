package com.zzx.zzxaiagent.agent;


import com.zzx.zzxaiagent.advisor.MyLoggerAdvisor;
import com.zzx.zzxaiagent.prompt.ManusPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

@Component
public class ZzxManus extends ToolCallAgent {
    public ZzxManus(ToolCallback[] allTools, ChatModel dashscopeChatModel) {
        super(allTools);
        setName("zzxManus");
        setSystemPrompt(ManusPrompt.SYSTEM_PROMPT);
        setNextStepPrompt(ManusPrompt.NEXT_STEP_PROMPT);
        setMaxSteps(10);

        // 初始化 AI 客户端
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(ManusPrompt.SYSTEM_PROMPT)
                .defaultAdvisors(
                        // 自定义日志拦截器
                        new MyLoggerAdvisor()
                )
                .build();
        this.setChatClient(chatClient);
    }
}
