package com.example.module.api.authorize.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {

    private String accessToken;
    private String refreshToken;
}
