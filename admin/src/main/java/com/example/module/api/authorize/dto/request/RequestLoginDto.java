package com.example.module.api.authorize.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLoginDto {
    @NotBlank(message = "아이디를 입력 해주세요.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력 해주세요.")
    private String password;
}
