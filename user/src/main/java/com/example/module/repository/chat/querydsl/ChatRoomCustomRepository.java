package com.example.module.repository.chat.querydsl;

import com.example.module.api.chat.dto.response.ResponseChatRoomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ChatRoomCustomRepository {
    Page<ResponseChatRoomDto> getChatRoomList(Map<String, Object> filters, Pageable pageable);
}
