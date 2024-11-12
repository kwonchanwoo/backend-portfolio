package com.example.module.util.security;

import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import io.lettuce.core.RedisCommandExecutionException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String str = "exception";
        try {
            // 1. Request Header 에서 JWT 토큰 추출
            String token = resolveToken((HttpServletRequest) request);

            // 2. validateToken 으로 토큰 유효성 검사
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("X-Authorized-id", authentication.getName());
            }
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            request.setAttribute(str, ErrorCode.TOKEN_INVALID);
        } catch (ExpiredJwtException e) {
            request.setAttribute(str, ErrorCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            request.setAttribute(str, ErrorCode.TOKEN_UNSUPPORTED);
        } catch (RedisCommandExecutionException e) {
            request.setAttribute(str, ErrorCode.REDIS_COMMAND_EXECUTION);
        } catch (CommonException e) {
            request.setAttribute(str, e.getEnumErrorCode());
        }
        chain.doFilter(request, response);
    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
