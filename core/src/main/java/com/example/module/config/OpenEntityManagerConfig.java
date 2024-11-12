package com.example.module.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.stereotype.Component;

/**
 *  OpenEntityManagerInView가 Spring Security의 DelegatingFilterProxy보다 먼저 적용될 수 있게 Filter 순위 변경
 *  DelegatingFilterProxy : 사용자의 요청을 가로채 Spring Security의 기능들이 수행되며 모든 요청에 대해 보안이 적용되게함
 */
@Component
@Configuration
public class OpenEntityManagerConfig {
    @Bean
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter() {
        FilterRegistrationBean<OpenEntityManagerInViewFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new OpenEntityManagerInViewFilter());
        filterFilterRegistrationBean.setOrder(Integer.MIN_VALUE); // 예시를 위해 최우선 순위로 Filter 등록
        return filterFilterRegistrationBean;
    }
}
