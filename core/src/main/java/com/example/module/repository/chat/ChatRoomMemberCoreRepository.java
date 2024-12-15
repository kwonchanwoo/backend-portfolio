package com.example.module.repository.chat;

import com.example.module.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberCoreRepository extends JpaRepository<ChatRoomMember, Long> {
}