package com.zzx.zzxsearchmcpserver.tools;

import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.chat.ChatService;
import ai.z.openapi.service.model.*;
import com.zzx.zzxsearchmcpserver.enums.SearchEngineType;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchTool {


    @Value("${zhi.api-key}")
    private String api_key;


    /**
     * 使用智普搜索
     * 对话中的网络搜索模型
     *
     * @param query
     * @return
     */
    @Tool(description = "search  from web")
    public String search(@ToolParam(description = "search query keyword") String query) {
        // 创建客户端
        ZhipuAiClient client = ZhipuAiClient.builder().apiKey(api_key).build();

        ChatService chatService = client.chat();

        // 定义用户消息
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), query);
        messages.add(userMessage);

        // 定义工具参数
        List<ChatTool> tools = new ArrayList<>();
        ChatTool webSearchTool = new ChatTool();
        webSearchTool.setType(ChatToolType.WEB_SEARCH.value());

        WebSearch webSearch = WebSearch.builder()
                .enable(true)
                .searchEngine(SearchEngineType.SEARCH_PRO.getValue()) // 使用高级版搜索引擎
                .searchResult(true)
                .searchPrompt("请用简洁的语言总结网络搜索{search_result}中的关键信息，按重要性排序并引用来源日期。今天的日期是2025年9月")
                .count(5)     // 返回 5 条信息， 默认10条
                .searchDomainFilter("www.baidu.com") // 限定搜索来源
                .searchRecencyFilter("noLimit")       // 搜索指定日期范围内的内容
                .contentSize("medium")   // 控制网页摘要的字数，默认medium
                .build();

        webSearchTool.setWebSearch(webSearch);
        tools.add(webSearchTool);

        // 调用API获取响应
        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model("glm-4-air")  // 模型标识符
                .messages(messages)  // 用户消息
                .tools(tools)        // 工具参数
                .toolChoice("auto")  // 自动选择工具
                .stream(false)       // 非流式响应
                .build();

        ChatCompletionResponse response = chatService.createChatCompletion(request);

        // 打印响应结果
        // System.out.println(response);
        return response.toString();
    }
}
