package com.example.module.repository.chat;

import com.example.module.entity.ChatRoom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageReadRepository extends ChatMessageReadCoreRepository {

    @Modifying
    @Query("update ChatMessageRead cmr set cmr.deleted = true where cmr.chatMessage in (select cm from ChatMessage cm where cm.chatRoom = :chatRoom)")
    void markMessageReadsAsDeleted(@Param("chatRoom") ChatRoom chatRoom);
}