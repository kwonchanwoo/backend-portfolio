package com.example.module.api.member.controller;

import com.example.module.api.member.dto.request.RequestDuplicateCheckDto;
import com.example.module.api.member.dto.request.RequestMemberDto;
import com.example.module.api.member.dto.response.ResponseMemberDto;
import com.example.module.api.member.service.MemberService;
import com.example.module.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberController {
    private final MemberService memberService;

    //Todo 마이페이지
    @GetMapping("/{member}")
    public ResponseMemberDto getMember(@PathVariable Member member){
        return new ResponseMemberDto(member);
    }

    // 가입
    @PostMapping("/join")
    public void join(@Valid @RequestBody RequestMemberDto memberCreateDto) {
        memberService.join(memberCreateDto);
    }

    //Todo: 회원탈퇴(사용자) 수정 필요

    // 회원 탈퇴
    @DeleteMapping("/{member}")
    public void deleteMember(
            @PathVariable Member member
    ) {
        memberService.deleteMember(member);
    }

    // 중복 회원 체크
    @PostMapping("/duplicate_check")
    @ResponseStatus(HttpStatus.OK)
    public void duplicateCheck(@Valid @RequestBody RequestDuplicateCheckDto duplicateCheckDto){
        memberService.duplicateCheck(duplicateCheckDto.getId());
    }

    //Todo 회원수정 추가 필요

}
