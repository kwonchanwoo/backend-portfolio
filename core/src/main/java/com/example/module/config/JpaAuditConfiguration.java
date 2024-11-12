package com.example.module.config;

import com.example.module.entity.Member;
import com.example.module.util.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 엔터티 객체가 생성 또는 변경이 되었을 때 감지
 * auditorAwareRef : @CreatedBy, @LastModifiedBy과 함께 생성한 사람, 수정한 사람을 자동으로 저장 하는 역할
 * modifyOnCreate : 값을 등록할 때, 수정값 도 넣을지 여부(default:true)
 */
@Configuration
@ComponentScan
@EnableJpaAuditing(auditorAwareRef = "auditorProvider",modifyOnCreate = false)
public class JpaAuditConfiguration {

    @Bean
    public AuditorAware<Member> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
