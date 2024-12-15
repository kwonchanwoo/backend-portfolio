package com.example.module.api.chat.dto.request;

import com.example.module.util._Enum.ChatRoomCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostChatRoomDto {
    private String title;
    private ChatRoomCategory chatRoomCategory;
}
