package com.example.module.api.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseChatSubScribeDto {
    private Long chatroomId;
    private String status;  // "SUBSCRIBE_SUCCESS" or "ALREADY_SUBSCRIBED"
}
