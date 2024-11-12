package com.example.module.util;

import com.example.module.entity.Member;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static com.example.module.util.SecurityContextHelper.getAuthentication;

public class AuditorAwareImpl implements AuditorAware<Member> {

    @Override
    public Optional<Member> getCurrentAuditor() {
////        현재 로그인한 SecurityContextHolder 에서 정보를 가져옴
        Authentication authentication = getAuthentication();
////        만일 로그인한 정보가 null 이 아니고 인증이 되어있고 name 이 anonymousUser(인증되지 않은 유저)가 아닐 경우 로그인 처리
        if (
                authentication == null
                        || !authentication.isAuthenticated()
                        || authentication.getName().equals("anonymousUser")
        ) {
////            로그인이 되어있지 않을 경우 empty 를 입력
            return Optional.empty();
        }
////        로그인이 되어있을 시 해당 정보의 Name 에 저장되어있는 seq 번호로 UserRepository 에서 유저정보를 찾아서 반환
        return Optional.ofNullable(authentication.getPrincipal()).map((Member.class::cast));
    }
}
