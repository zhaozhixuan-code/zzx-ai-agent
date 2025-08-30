package com.zzx.zzxaiagent.chatmemory;

import com.zzx.zzxaiagent.entity.po.ChatMessage;
import com.zzx.zzxaiagent.mapper.MessageMapper;
import com.zzx.zzxaiagent.tools.MessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MysqlChatMemory implements ChatMemory {


    private final MessageMapper messageMapper;   // 注入消息mapper

    @Override
    public void add(String conversationId, List<Message> messages) {

        List<ChatMessage> list = messages.stream()
                .map(message -> MessageConverter.toChatMessage(message, conversationId))
                .collect(Collectors.toList());
        messageMapper.insertAll(list);
    }

    @Override
    public List<Message> get(String conversationId) {
        List<ChatMessage> list = messageMapper.list(conversationId);
        List<Message> messageList = list
                .stream()
                .map(MessageConverter::toMessage)
                .toList();
        return messageList;
    }



    @Override
    public void clear(String conversationId) {

    }

}
