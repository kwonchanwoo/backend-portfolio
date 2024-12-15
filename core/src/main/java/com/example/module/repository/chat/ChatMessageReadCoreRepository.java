package com.example.module.repository.chat;

import com.example.module.entity.ChatMessageRead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageReadCoreRepository extends JpaRepository<ChatMessageRead, Long> {
}