package com.zzx.zzxaiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.zzx.zzxaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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


    /**
     * 运行代理
     *
     * @param userPrompt 用户的提示词
     * @return 执行结果
     */
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
     * 运行代理 流式输出
     *
     * @param userPrompt 用户的提示词
     * @return 执行结果
     */
    public SseEmitter runStream(String userPrompt) {
        // 指定超时时间 5 分钟
        SseEmitter sseEmitter = new SseEmitter(1000 * 60 * 5L);

        // 使用线程异步处理
        CompletableFuture.runAsync(() -> {
            try {
                // 执行前的校验判断
                if (this.state != AgentState.IDLE) {
                    sseEmitter.send("Cannot run agent from state: " + this.state);
                    sseEmitter.complete();
                    return;
                }
                if (StrUtil.isBlank(userPrompt)) {
                    sseEmitter.send("Cannot run agent with empty user prompt");
                    sseEmitter.complete();
                }
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
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

                    // 把执行的结果放入结果列表中
                    results.add("step " + this.currentStep + ":" + messagesList.getLast());
                    results.add("step " + this.currentStep + ":" + stepResult);

                    // 推流
                    sseEmitter.send("step " + this.currentStep + ":" + messagesList.getLast());
                    sseEmitter.send("step " + this.currentStep + ":" + stepResult);
                }
                // 检查是否超出步骤的限制
                if (currentStep >= maxSteps) {
                    currentStep = 0;
                    state = AgentState.IDLE;
                    results.add("Terminated: Reached max steps (" + maxSteps + ")");

                    // 推流错误信息
                    sseEmitter.send("Terminated: Reached max steps (" + maxSteps + ")");
                }
                // String message = String.join("\n", results);
                // return message;
            } catch (Exception e) {
                // 如果出现执行错误
                this.state = AgentState.ERROR;
                try {
                    // 推送错误信息
                    sseEmitter.send("执行错误" + e.getMessage());
                    // 结束执行
                    sseEmitter.complete();
                } catch (IOException ex) {
                    sseEmitter.completeWithError(ex);
                }
                log.error("Error executing agent", e);
                // return "执行错误" + e.getMessage();
            } finally {
                // 清理资源
                this.cleanup();
            }
        });
        // 设置超时和完成回调
        sseEmitter.onTimeout(() -> {
            this.state = AgentState.ERROR;
            this.cleanup();
            log.warn("SSE connection timed out");
        });

        sseEmitter.onCompletion(() -> {
            if (this.state == AgentState.RUNNING) {
                this.state = AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE connection completed");
        });

        return sseEmitter;

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
