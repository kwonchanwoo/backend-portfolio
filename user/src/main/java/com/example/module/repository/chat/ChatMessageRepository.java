package com.example.module.repository.chat;

import com.example.module.entity.ChatRoom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends ChatMessageCoreRepository {

    @Modifying
    @Query("update ChatMessage cm set cm.deleted = true where cm.chatRoom = :chatRoom")
    void markMessagesAsDeleted(@Param("chatRoom") ChatRoom chatRoom);
}
