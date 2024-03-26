package com.ssafy.maryflower.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")  // CORS 적용할 URL 패턴
                .allowedHeaders("Authorization", "Content-Type")    // CORS 요청에 허용되는 헤더
                .allowedOriginPatterns("*")    // 자원 공유를 허락할 Origin 지정
                .allowedMethods("*")    // 허용할 HTTP method
                .allowCredentials(true) // Credentials 포함 여부
                .maxAge(3600);  // Pre-flight 리퀘스트 캐싱
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");
    }
}
