package com.example.module.repository.chat;

import com.example.module.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomCoreRepository extends JpaRepository<ChatRoom, Long> {
}