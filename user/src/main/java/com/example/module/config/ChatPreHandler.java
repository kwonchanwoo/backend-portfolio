package com.example.module.config;

import com.example.module.dto.Role;
import com.example.module.entity.Member;
import com.example.module.repository.member.MemberRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Key;
import java.util.List;

@Slf4j
@Configuration
public class ChatPreHandler implements ChannelInterceptor {

    private final Key key;
    private final MemberRepository memberRepository;

    public ChatPreHandler(@Value("${jwt.secret}") String secretKey, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            String authorizationHeader = String.valueOf(accessor.getNativeHeader("Authorization"));

            System.out.println("authorizationHeader : " + authorizationHeader);

            StompCommand command = accessor.getCommand();

            if (StompCommand.CONNECT.equals(command)) {
                //token 분리
                String token = "";
                String authorizationHeaderStr = authorizationHeader.replace("[", "").replace("]", "");
                if (authorizationHeaderStr.startsWith("Bearer ")) {
                    token = authorizationHeaderStr.replace("Bearer ", "");
                } else {
                    throw new MalformedJwtException("jwt");
                }

                validateToken(token);

                String userId = getId(token);
                List<Role> userRole = getRoles(token).stream().map(Role::valueOf).toList();
                Authentication authentication = createAuthentication(userId, userRole);
                accessor.setUser(authentication);

                return message;
            } else if (StompCommand.ERROR.equals(command)) {
                throw new MessageDeliveryException("error");
            } else {
                // SecurityContextHolder에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication((Authentication) accessor.getUser());
                return message;
            }

        } catch (MessageDeliveryException e) {
            log.error("메시지 에러");
            throw new MessageDeliveryException("error");
        } catch (ExpiredJwtException e) {
            throw new CommonException(ErrorCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new CommonException(ErrorCode.TOKEN_UNSUPPORTED);
        }
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }

//    @Override
//    public boolean preReceive(MessageChannel channel) {
//        return ChannelInterceptor.super.preReceive(channel);
//    }
//
//    @Override
//    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
//        return ChannelInterceptor.super.postReceive(message, channel);
//    }
//
//    @Override
//    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
//        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
//    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            log.info("Invalid JWT Token", e);
            throw e;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw e;
        }
    }

    public String getId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // JWT `sub` 필드에서 사용자 ID 추출
    }

    public List<String> getRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object authClaim = claims.get("auth");

        if (authClaim instanceof String) {
            // 문자열일 경우 단일 권한을 리스트로 반환
            return List.of(((String) authClaim).replace("ROLE_", ""));
        } else if (authClaim instanceof List) {
            // 리스트일 경우 직접 반환
            return (List<String>) authClaim;
        } else {
            throw new IllegalArgumentException("Invalid auth claim type");
        }
    }

    private Authentication createAuthentication(String userId, List<Role> roles) {
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name())) // ROLE_ 형태 유지
                .toList();
        // Member 엔티티를 직접 principal로 설정
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        return new UsernamePasswordAuthenticationToken(member, null, authorities);
    }
}
