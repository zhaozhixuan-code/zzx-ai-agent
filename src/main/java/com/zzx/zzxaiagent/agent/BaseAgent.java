package com.zzx.zzxaiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.esotericsoftware.minlog.Log;
import com.zzx.zzxaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义代理状态管理和执行循环的核心逻辑
 * 是所有代理的基础
 * 子类必须实现
 */
@Slf4j
@Data
public abstract class BaseAgent {
    // 核心属性
    private String name;
    private String description;

    // 提示词
    private String systemPrompt;
    private String nextStepPrompt;

    // 状态
    private AgentState state = AgentState.IDLE;

    // 执行控制
    private int maxSteps = 10; // 最大执行步数
    private int currentStep = 0; // 当前步数

    // memory 需要自出维护会话上下文
    private List<Message> messagesList = new ArrayList<>();

    // 定义客户端
    private ChatClient chatClient;

    public String run(String userPrompt) {
        // 执行前的校验判断
        if (this.state != AgentState.IDLE) {
            throw new RuntimeException("Cannot run agent from state: " + this.state);
        }
        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("Cannot run agent with empty user prompt");
        }
        // 更改状态
        this.state = AgentState.RUNNING;
        // 消息记录到上下文
        messagesList.add(new UserMessage(userPrompt));
        ArrayList<String> results = new ArrayList<>();

        try {
            while (currentStep < maxSteps && state != AgentState.FINISHED) {
                // 当前步骤 +1
                this.currentStep++;
                // 执行步骤
                String stepResult = step();

                results.add("step " + this.currentStep + ":" + messagesList.getLast());
                // 把执行的结果放入结果列表中
                results.add("step " + this.currentStep + ":" + stepResult);

            }
            // 检查是否超出步骤的限制
            if (currentStep >= maxSteps) {
                currentStep = 0;
                state = AgentState.IDLE;
                results.add("Terminated: Reached max steps (" + maxSteps + ")");
            }
            String message = String.join("\n", results);
            return message;
        } catch (Exception e) {
            // 出现异常，修改状态
            this.state = AgentState.ERROR;
            log.error("Error executing agent", e);
            return "执行错误" + e.getMessage();
        } finally {
            // 清理资源
            this.cleanup();
        }
    }


    /**
     * 执行单个步骤
     *
     * @return
     */
    public abstract String step();

    /**
     * 清理资源
     * 子类可重写此方法来清理资源
     */
    public void cleanup() {

    }
}
