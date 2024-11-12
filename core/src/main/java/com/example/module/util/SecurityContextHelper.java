package com.example.module.util;

import com.example.module.entity.Member;
import com.example.module.util._Enum.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHelper {
    private SecurityContextHelper(){}

    // 로그인 유저의 권한 조회
    public  static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 시큐리티에 저장한 로그인 유저 정보를 조회
    public static Member getPrincipal(){
        return (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    // 로그인한 유저가 Admin인지 체크
    public static boolean isAdmin(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public static void isAuthorizedForMember(Member member){
        if(!isAdmin() && !getPrincipal().equals(member)){
            throw new CommonException(ErrorCode.TOKEN_ACCESS_DENIED);
        }
    }
}
