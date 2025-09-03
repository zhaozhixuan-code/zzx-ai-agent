package com.zzx.zzxaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.zzx.zzxaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;


@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    // 可用的工具
    private final ToolCallback[] allTools;

    // 工具调用的管理者
    private final ToolCallingManager toolCallingManager;

    // 保存工具调用信息的响应
    private ChatResponse chatResponse;

    // 禁用 Spring AI 内置的工具调用机制，自己维护选项和消息上下文
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] allTools) {
        super();
        this.allTools = allTools;
        toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()
                .withInternalToolExecutionEnabled(false)
                .build();
    }


    @Override
    public boolean think() {
        // 1、 判断是否有下一步的提示词
        if (getNextStepPrompt() != null && !getNextStepPrompt().isEmpty()) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessagesList().add(userMessage);
        }
        try {
            // 2、 调用大模型
            List<Message> messagesList = getMessagesList();
            Prompt prompt = new Prompt(messagesList);
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .toolCallbacks(allTools)
                    .call()
                    .chatResponse();
            // 3、 记录响应
            this.chatResponse = chatResponse;
            // 4. 解析工具调用结果
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();

            // 获取返回结果
            String result = assistantMessage.getText();
            // 获取调用的工具列表
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();
            // 不需要调用工具
            if (CollUtil.isEmpty(toolCallList)) {
                // 需要记录消息
                getMessagesList().add(assistantMessage);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            getMessagesList().add(new AssistantMessage("处理室遇到了问题" + e.getMessage()));
            return false;
        }
    }

    @Override
    public String act() {
        if (!chatResponse.hasToolCalls()) {
            return "没有工具调用";
        }
        Prompt prompt = new Prompt(getMessagesList(), this.chatOptions);
        // 调用工具
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse);
        // 记录消息上下文，conversationHistory已经包含了助手消息和工具调用返回结果
        setMessagesList(toolExecutionResult.conversationHistory());

        ToolResponseMessage tollResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        // 判断是否调用了终止工具
        boolean terminate = tollResponseMessage.getResponses().stream()
                .anyMatch(response -> response.name().equals("doTerminate"));
        if (terminate) {
            // 任务结束
            setState(AgentState.FINISHED);
        }

        String result = tollResponseMessage.getResponses().stream()
                .map(response -> "工具 " + response.name() + "返回结果：" + response.responseData())
                .collect(Collectors.joining("\n"));
        log.info(result);
        return result;
    }
}
