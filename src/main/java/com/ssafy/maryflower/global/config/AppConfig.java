package com.ssafy.maryflower.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    // Gpt api 사용 위함.
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
