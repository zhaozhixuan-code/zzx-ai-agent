package com.zzx.zzxaiagent.entity.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.MessageType;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天消息实体类
 * 对应 chat_message 表
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage implements Serializable {
    private Long id;                // 主键ID

    private String conversationId;   // 会话ID

    private String messageType;      // 消息类型

    private String content;          // 消息内容


    /**
     * 元数据(JSON字符串)
     * Map<String, Object>
     */
    private String  properties;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 是否删除
     * 0-未删除
     * 1-已删除
     */
    private Boolean isDelete;
}
