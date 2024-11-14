package com.example.module.api.member.controller;

import com.example.module.api.member.dto.request.RequestDuplicateCheckDto;
import com.example.module.api.member.dto.request.RequestMemberDto;
import com.example.module.api.member.dto.request.RequestPatchMemberDto;
import com.example.module.api.member.dto.response.ResponseMemberDto;
import com.example.module.api.member.service.MemberService;
import com.example.module.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public Page<ResponseMemberDto> getMemberList(@RequestParam(required = false)HashMap<String,Object> filters, Pageable pageable){
        return memberService.getMemberList(filters, pageable);
    }

    @GetMapping("/{member}")
    public ResponseMemberDto getMember(@PathVariable Member member){
        return new ResponseMemberDto(member);
    }

    // 가입
    @PostMapping("/join")
    public void join(@Valid @RequestBody RequestMemberDto memberCreateDto) {
        memberService.join(memberCreateDto);
    }

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

    /**
     * 회원 권한 수정
     *
     * @param member
     * @param requestPatchMemberDto
     */
    @PatchMapping("/{member}")
    @ResponseStatus(HttpStatus.OK)
    public void patchMember(@RequestParam(name = "member") Member member, @Valid @RequestBody RequestPatchMemberDto requestPatchMemberDto) {
        memberService.patchMember(member,requestPatchMemberDto);
    }
}
