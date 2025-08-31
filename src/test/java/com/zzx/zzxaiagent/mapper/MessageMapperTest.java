package com.zzx.zzxaiagent.mapper;

import com.zzx.zzxaiagent.entity.po.ChatMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageMapperTest {

    @Resource
    private MessageMapper messageMapper;


    @Test
    void insertAll() {

    }

    @Test
    void list() {
        List<ChatMessage> list = messageMapper.list("0ca872ea-a379-4564-b410-d860b2e8da2f");
        for (ChatMessage chatMessage : list) {
            System.out.println(chatMessage);
        }
    }

    @Test
    void findByConversationId() {
    }
}