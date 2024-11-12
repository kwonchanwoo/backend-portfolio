package com.example.module.api.member.dto.response;

import com.example.module.entity.Member;
import com.example.module.util._Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMemberDto {
    private Long id;
    private String userId;
    private String email;
    private String name;
    private Gender sex;
    private int age;
    private String phoneNumber;

    public ResponseMemberDto(Member member) {
        this.id = member.getId();
        this.userId = member.getUserId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.sex = member.getSex();
        this.age = member.getAge();
        this.phoneNumber = member.getPhoneNumber();
    }
}
