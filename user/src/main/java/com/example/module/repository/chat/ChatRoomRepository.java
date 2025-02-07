package com.example.module.repository.chat;

import com.example.module.entity.ChatRoom;
import com.example.module.repository.chat.querydsl.ChatRoomCustomRepository;

import java.util.Optional;

public interface ChatRoomRepository extends ChatRoomCoreRepository, ChatRoomCustomRepository {


    Optional<ChatRoom> findByIdAndDeletedFalse(Long id);
}