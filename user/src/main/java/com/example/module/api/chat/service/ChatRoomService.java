package com.example.module.api.chat.service;

import com.example.module.api.chat.dto.request.RequestChatMessageDto;
import com.example.module.api.chat.dto.request.RequestPostChatRoomDto;
import com.example.module.api.chat.dto.response.ResponseChatMessageDto;
import com.example.module.api.chat.dto.response.ResponseChatRoomDto;
import com.example.module.api.chat.dto.response.ResponseChatSubScribeDto;
import com.example.module.entity.ChatMessage;
import com.example.module.entity.ChatRoom;
import com.example.module.entity.ChatRoomMember;
import com.example.module.entity.Member;
import com.example.module.repository.chat.ChatMessageReadRepository;
import com.example.module.repository.chat.ChatMessageRepository;
import com.example.module.repository.chat.ChatRoomMemberRepository;
import com.example.module.repository.chat.ChatRoomRepository;
import com.example.module.repository.member.MemberRepository;
import com.example.module.util.CommonException;
import com.example.module.util.SecurityContextHelper;
import com.example.module.util._Enum.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberRepository memberRepository;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatMessageReadRepository chatMessageReadRepository;

    public Page<ResponseChatRoomDto> getChatRoomList(Map<String, Object> filters, Pageable pageable) {
        return chatRoomRepository.getChatRoomList(filters, pageable);
    }

    @Transactional
    public void postChatRoom(RequestPostChatRoomDto requestPostChatRoomDto) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .title(requestPostChatRoomDto.getTitle())
                .chatRoomCategory(requestPostChatRoomDto.getChatRoomCategory())
                .build());

        List<Long> invitationIds = requestPostChatRoomDto.getInvitationIds();

        chatRoomMemberRepository.save(ChatRoomMember
                        .builder()
                        .chatRoom(chatRoom)
                        .subScriber(SecurityContextHelper.getPrincipal())
                        .build());

        for (Long invitationId : invitationIds) {
            Member member = memberRepository.findById(invitationId).orElseThrow(() -> new CommonException(ErrorCode.MEMBER_NOT_FOUND));
            if (!chatRoomMemberRepository.existsByChatRoomAndSubScriber(chatRoom, member)) {
                chatRoomMemberRepository.save(ChatRoomMember
                        .builder()
                        .chatRoom(chatRoom)
                        .subScriber(member)
                        .build());
            }
        }
    }

    @Transactional
    public ResponseChatSubScribeDto subScribe(ChatRoom chatRoom) {
        if (chatRoomMemberRepository.existsByChatRoomAndSubScriber(chatRoom, SecurityContextHelper.getPrincipal())) {
            return new ResponseChatSubScribeDto(chatRoom.getId(), "ALREADY_SUBSCRIBED");
        }

        chatRoomMemberRepository.save(ChatRoomMember.builder().chatRoom(chatRoom).build());
        return new ResponseChatSubScribeDto(chatRoom.getId(), "SUBSCRIBED");
    }

    @Transactional
    public ResponseChatMessageDto sendMessage(Long roomId, RequestChatMessageDto requestChatMessageDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new CommonException(ErrorCode.CHAT_ROOM_NOT_EXISTS));

        if (null == requestChatMessageDto.getSenderId()) {
            chatMessageRepository.save(
                    ChatMessage.builder()
                            .chatRoom(chatRoom)
                            .contents(requestChatMessageDto.getContents())
                            .build());
        } else {
            memberRepository.findById(requestChatMessageDto.getSenderId()).ifPresentOrElse(member ->
                            chatMessageRepository.save(
                                    ChatMessage.builder()
                                            .chatRoom(chatRoom)
                                            .contents(requestChatMessageDto.getContents())
                                            .recipient(member)
                                            .build()),
                    () -> chatMessageRepository.save(
                            ChatMessage.builder()
                                    .chatRoom(chatRoom)
                                    .contents(requestChatMessageDto.getContents())
                                    .build())
            );
        }
        return new ResponseChatMessageDto(requestChatMessageDto.getContents());
//        simpMessageSendingOperations.convertAndSend("/sub/chatrooms/" + roomId,requestChatMessageDto.getContents());
    }

    @Transactional
    public void deleteChatRoom(ChatRoom chatRoom) {

        SecurityContextHelper.isAuthorizedForMember(chatRoom.getCreatedMember());

        chatRoomMemberRepository.markRoomMembersAsDeleted(chatRoom);
        chatMessageRepository.markMessagesAsDeleted(chatRoom);
        chatMessageReadRepository.markMessageReadsAsDeleted(chatRoom);

        chatRoom.setDeleted(true);
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void unsubscribe(ChatRoom chatRoom) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository
                .findByChatRoomAndCreatedMember(chatRoom, chatRoom.getCreatedMember())
                .orElseThrow(() -> new CommonException(ErrorCode.CHAT_ROOM_MEMBER_NOT_EXISTS));
        chatRoomMember.setDeleted(true);
        chatRoomMemberRepository.save(chatRoomMember);
    }
}
