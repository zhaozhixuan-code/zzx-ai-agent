package com.zzx.zzxaiagent.mapper;


import com.zzx.zzxaiagent.entity.po.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {



    /**
     * 批量插入对话信息
     *
     * @param list
     */
    void insertAll(@Param("chatMessage") List<ChatMessage> list);


    /**
     * 根据会话Id查询会话信息
     * 之后再根据createTime排序
     *
     * @param conversationId
     * @return
     */
    @Select("select * from chat_message where conversation_id = #{conversationId} ")
    List<ChatMessage> list(String conversationId);

    /**
     * 根据会话ID查询最近的N条消息
     * @param conversationId 会话ID
     * @param lastN 最近的条数，0表示查询所有
     * @return 消息列表
     */
    List<ChatMessage> findByConversationId(
            @Param("conversationId") String conversationId,
            @Param("lastN") int lastN
    );

}
