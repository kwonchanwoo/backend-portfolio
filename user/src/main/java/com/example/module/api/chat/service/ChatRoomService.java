package com.example.module.api.chat.service;

import com.example.module.api.chat.dto.request.RequestChatMessageDto;
import com.example.module.api.chat.dto.request.RequestPostChatRoomDto;
import com.example.module.api.chat.dto.response.ResponseChatMessageDto;
import com.example.module.api.chat.dto.response.ResponseChatSubScribeDto;
import com.example.module.entity.ChatMessage;
import com.example.module.entity.ChatRoom;
import com.example.module.entity.ChatRoomMember;
import com.example.module.entity.Member;
import com.example.module.repository.chat.ChatMessageRepository;
import com.example.module.repository.chat.ChatRoomMemberRepository;
import com.example.module.repository.chat.ChatRoomRepository;
import com.example.module.repository.member.MemberRepository;
import com.example.module.util.CommonException;
import com.example.module.util.SecurityContextHelper;
import com.example.module.util._Enum.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberRepository memberRepository;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Transactional
    public void postChatRoom(RequestPostChatRoomDto requestPostChatRoomDto) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .title(requestPostChatRoomDto.getTitle())
                .chatRoomCategory(requestPostChatRoomDto.getChatRoomCategory())
                .build());

        chatRoomMemberRepository.save(ChatRoomMember.builder().chatRoom(chatRoom).build());
    }

    @Transactional
    public ResponseChatSubScribeDto subscribe(ChatRoom chatRoom) {
        if (chatRoomMemberRepository.existsByChatRoomAndCreatedMember(chatRoom, SecurityContextHelper.getPrincipal())) {
            return new ResponseChatSubScribeDto(chatRoom.getId(), "ALREADY_SUBSCRIBED");
        }

        chatRoomMemberRepository.save(ChatRoomMember.builder().chatRoom(chatRoom).build());
        return new ResponseChatSubScribeDto(chatRoom.getId(), "SUBSCRIBED");
    }

    @Transactional
    public ResponseChatMessageDto sendMessage(Long roomId, RequestChatMessageDto requestChatMessageDto) {
        System.out.println("Message to Room " + roomId + ": " + requestChatMessageDto.getContents());

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new CommonException(ErrorCode.CHAT_ROOM_NOT_EXISTS));

        Member member = memberRepository.findById(requestChatMessageDto.getSenderId())
                .orElseThrow(() -> new CommonException(ErrorCode.MEMBER_NOT_FOUND));

        chatMessageRepository.save(
                ChatMessage.builder()
                        .chatRoom(chatRoom)
                        .contents(requestChatMessageDto.getContents())
                        .createdMember(member)
                        .build());

        return new ResponseChatMessageDto(requestChatMessageDto.getContents());
//        simpMessageSendingOperations.convertAndSend("/sub/chatrooms/" + roomId,requestChatMessageDto.getContents());
    }
}
