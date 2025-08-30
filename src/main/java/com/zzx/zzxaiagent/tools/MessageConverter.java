package com.zzx.zzxaiagent.tools;

import cn.hutool.json.JSONUtil;
import com.zzx.zzxaiagent.entity.po.ChatMessage;
import org.springframework.ai.chat.messages.*;

import java.util.List;
import java.util.Map;

/**
 * 消息转换器，用于Message和ChatMessage之间的转换
 */
public class MessageConverter {

    /**
     * 将Message转换成数据库实体ChatMessage
     *
     * @param message        Spring AI的Message对象
     * @param conversationId 会话ID
     * @return 数据库实体ChatMessage
     */
    public static ChatMessage toChatMessage(Message message, String conversationId) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setConversationId(conversationId);
        chatMessage.setMessageType(message.getMessageType()); // 设置消息类型（USER/ASSISTANT等）
        chatMessage.setContent(message.getText()); // 设置消息内容

        // 正确逻辑：只序列化metadata（元数据），而非整个Message对象
        Map<String, Object> metadata = message.getMetadata();
        // 若metadata为空，存入空JSON对象（避免null或空字符串导致后续解析错误）
        String propertiesJson = metadata.isEmpty() ? "{}" : JSONUtil.toJsonStr(metadata);
        chatMessage.setProperties(propertiesJson);

        return chatMessage;
    }

    /**
     * 将数据库实体ChatMessage转换成Spring AI的Message
     *
     * @param chatMessage 数据库实体
     * @return Spring AI的Message对象
     */
    public static Message toMessage(ChatMessage chatMessage) {
        // 获取类型
        MessageType messageType = chatMessage.getMessageType();
        // 获取消息内容
        String content = chatMessage.getContent();
        // 获取元数据
        String properties = chatMessage.getProperties();
        Map<String, Object> metadata = JSONUtil.toBean(properties, Map.class);

        return switch (messageType) {
            case USER -> new UserMessage(content);
            case TOOL -> new ToolResponseMessage(List.of(), metadata);
            case ASSISTANT -> new AssistantMessage(content, metadata);
            case SYSTEM -> new SystemMessage(content);
        };
    }

}