package com.example.module.config;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Enum 대소문자 구분하지 않고 사용하는 용도
 */
@Configuration
public class ConversionConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        ApplicationConversionService.configure(registry);
    }
}
