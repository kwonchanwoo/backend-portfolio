package com.example.module.api.member.dto.request;

import com.example.module.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPatchMemberDto {
    private Role role;
}
