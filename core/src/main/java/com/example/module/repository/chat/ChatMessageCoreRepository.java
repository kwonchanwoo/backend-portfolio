package com.example.module.repository.chat;

import com.example.module.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageCoreRepository extends JpaRepository<ChatMessage, Long> {
}