package com.example.module.api.authorize.service;


import com.example.module.api.authorize.dto.request.RequestLoginDto;
import com.example.module.api.authorize.dto.response.TokenInfo;
import com.example.module.entity.Member;
import com.example.module.entity.Token;
import com.example.module.repository.TokenRepository;
import com.example.module.repository.member.MemberRepository;
import com.example.module.util.CommonException;
import com.example.module.util.SecurityContextHelper;
import com.example.module.util._Enum.ErrorCode;
import com.example.module.util.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorizeService implements UserDetailsService {
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticate;
    private final MemberRepository memberRepository;

    /**
     * 로그인
     *
     * @param loginDto
     * @return
     */
    @Transactional
    public TokenInfo login(RequestLoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUserId(),
                        loginDto.getPassword()
                );
        Authentication authentication = authenticate.getObject().authenticate(authenticationToken);
        TokenInfo token = jwtTokenProvider.generateToken(authentication);

        List<Token> oldTokenList = tokenRepository.findByTokenKey(authentication.getName());
        if ((long) oldTokenList.size() > 0) {
            oldTokenList.forEach(refreshToken -> refreshToken.setDeleted(true));
        }

        tokenRepository.save(
                Token.builder()
                        .tokenKey(authentication.getName())
                        .tokenValue(token.getRefreshToken())
                        .deleted(false)
                        .build()
        );

        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return memberRepository.findByUserId(id)
                // 접속한 유저의 권한이 User(일반회원) 일때, 계정이 삭제됫는지 체크
                .filter(Member::isEnabled)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRoles().toArray(new String[0]))
                .build();
    }


    @Transactional
    public TokenInfo refreshToken(TokenInfo tokenInfo) {
        if (!jwtTokenProvider.validateToken(tokenInfo.getRefreshToken())) {
            throw new CommonException(ErrorCode.TOKEN_INVALID);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());

        Token token = tokenRepository
                .findFirstByTokenKeyOrderByCreatedAtDesc(authentication.getName())
                .orElseThrow(() -> new CommonException(ErrorCode.TOKEN_INVALID));

        if (!Objects.equals(token.getTokenValue(), tokenInfo.getRefreshToken())) {
            throw new CommonException(ErrorCode.TOKEN_INVALID);
        }

        TokenInfo newToken = jwtTokenProvider.generateToken(authentication);
        newToken.setRefreshToken(token.getTokenValue());

        return newToken;
    }

    @Transactional
    public void logout() {

        // 현재 로그인한 유저인지 체크
        if(!Objects.nonNull(SecurityContextHelper.getPrincipal())){
            throw new CommonException(ErrorCode.ACCESS_DENIED);
        }

        List<Token> oldTokenList = tokenRepository.findByTokenKey(SecurityContextHelper.getPrincipal().getUserId());
        if ((long) oldTokenList.size() > 0) {
            oldTokenList.forEach(refreshToken -> refreshToken.setDeleted(true));
        }
    }
}
