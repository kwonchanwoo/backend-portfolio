package com.example.module.util.security;

import com.example.module.util.security.exception.CustomAccessDeniedHandler;
import com.example.module.util.security.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableWebSecurity
@Log4j2
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    // 역할 계층 등록 ADMIN > USER
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        Map<String, List<String>> roleHierarchyMap = new HashMap<>();
        roleHierarchyMap.put("ROLE_ADMIN", List.of("ROLE_USER"));

        String roles = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);
        log.debug(roles);
        roleHierarchy.setHierarchy(roles);

        // 혹은 아래와 같이 작성할 수 있다.
        // roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER\nROLE_MANAGER > ROLE_POST\nROLE_MANAGER > ROLE_COMMENT\nROLE_MANAGER > ROLE_FILE\nROLE_USER > ROLE_POST");
        return roleHierarchy;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/members/join",
                        "/members/duplicate_check",
                        "/authorize/login",
                        "/authorize/refresh-token"
                );
    }

    // 시큐리티 커스텀 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .cors().configurationSource(corsConfigurationSource())
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/admin/**").hasRole("ADMIN"))
                .exceptionHandling(authenticationManager -> authenticationManager
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedOrigin("http://localhost:8000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
