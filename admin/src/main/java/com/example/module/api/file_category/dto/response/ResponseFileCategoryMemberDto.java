package com.example.module.api.file_category.dto.response;

import com.example.module.util._Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFileCategoryMemberDto {
    private Long id; // 회원 pk
    private String userId; // 아이디
    private String name; // 회원 이름
    private String email; // 회원 이메일
    private Gender sex; // 회원 성별
    private int age;   // 회원 나이
    private boolean permission_status; // 권한 여부

}
