package com.example.module.api.chat.dto.response;

import com.example.module.util._Enum.ChatRoomCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseChatRoomDto {
    private Long id;
    private String title;
    private ChatRoomCategory chatRoomCategory;
    private String recentMessageSendDate;
    private Long unreadMessageCount;
    private Long memberCount;
    //Todo 읽지않은 메시지 갯수 추가 예정
}
