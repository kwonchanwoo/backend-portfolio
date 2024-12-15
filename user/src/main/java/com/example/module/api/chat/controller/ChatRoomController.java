package com.example.module.api.chat.controller;

import com.example.module.api.chat.dto.request.RequestChatMessageDto;
import com.example.module.api.chat.dto.request.RequestPostChatRoomDto;
import com.example.module.api.chat.dto.response.ResponseChatMessageDto;
import com.example.module.api.chat.dto.response.ResponseChatRoomDto;
import com.example.module.api.chat.dto.response.ResponseChatSubScribeDto;
import com.example.module.api.chat.service.ChatRoomService;
import com.example.module.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping
    public Page<ResponseChatRoomDto> getChatRoomList(@RequestParam(required = false) Map<String,Object> filters, Pageable pageable){
        return chatRoomService.getChatRoomList(filters,pageable);
    }

    /**
     * 채팅방 생성
     * @param requestPostChatRoomDto
     */
    @PostMapping
    public void postChatRoom(@RequestBody RequestPostChatRoomDto requestPostChatRoomDto){
        chatRoomService.postChatRoom(requestPostChatRoomDto);
    }

    /**
     * 채팅방 인원 초대 추가 예정
     */

    /**
     * 채팅방 구독
     * @param chatRoom
     * @return ResponseChatSubScribeDto
     */
    @PostMapping("/{roomId}/subscribe")
    public ResponseChatSubScribeDto subScribe(@PathVariable(value = "roomId") ChatRoom chatRoom) {
        return chatRoomService.subScribe(chatRoom);
    }

    /**
     * 메시지 발송
     * @param roomId
     * @param requestChatMessageDto
     * @return ResponseChatMessageDto
     */
    @MessageMapping("/{roomId}")
    @SendTo("/topic/{roomId}")
    public ResponseChatMessageDto sendMessage(@DestinationVariable Long roomId, RequestChatMessageDto requestChatMessageDto) {
        return chatRoomService.sendMessage(roomId, requestChatMessageDto); // 구독자들에게 브로드캐스트
    }
}
