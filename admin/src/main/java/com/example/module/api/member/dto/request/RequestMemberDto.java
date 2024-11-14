package com.example.module.api.member.dto.request;

import com.example.module.dto.Role;
import com.example.module.entity.Member;
import com.example.module.util._Enum.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestMemberDto {
    @NotBlank(message = "아이디를 입력해 주세요.")
    private String userId;
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[!@#$%^&*])(?=.*[a-zA-Z]).{8,}$"
            , message = "비밀번호 형식을 지켜주세요(숫자,특수문자 최소1개,최소 길이 8이상)"
    )
    private String password;
    @Valid
    private Gender sex;
    @Min(value = 1, message = "나이를 잘못 입력 하셨습니다.")
    private int age;
    @Pattern(regexp = "^01[0-9]{1}-[0-9]{4}-[0-9]{4}$", message = "전화번호 형식이 안맞습니다. (xxx-xxxx-xxxx)")
    private String phoneNumber;

    private Role role;

    public RequestMemberDto(Member member) {
        this.userId = member.getUserId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.age = member.getAge();
        this.phoneNumber = member.getPhoneNumber();
    }
}
