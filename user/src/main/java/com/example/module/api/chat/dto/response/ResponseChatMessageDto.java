package com.example.module.api.chat.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseChatMessageDto {
    //    private Long id;
//    private Long chatroomId;   // 채팅방 ID
//    private String sender;     // 보낸 사람
    private String contents;    // 메시지 내용

    //    private LocalDateTime sentAt; // 전송 시간
    public ResponseChatMessageDto(String contents) {
//        this.id = chatMessage.getId();
//        this.chatroomId = chatMessage.getChatRoom().getId();
//        this.sender = chatMessage.getCreatedMember().getName();
//        this.content = chatMessage.getContents();
        this.contents = contents;
//        this.sentAt = chatMessage.getCreatedAt();
    }
}
