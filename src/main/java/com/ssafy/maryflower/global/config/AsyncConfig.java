package com.ssafy.maryflower.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
// spring의 비동기 처리 기능을 활성화 하는 어노테이션.
@EnableAsync
public class AsyncConfig {
//    @Bean
//    public ThreadPoolTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10); // 스레드 풀의 기본 스레드 수
//        executor.setMaxPoolSize(20); // 최대 스레드 수
//        executor.setQueueCapacity(500); // 작업 큐 용량
//        executor.setThreadNamePrefix("Async-Executor-");
//        executor.initialize();
//        return executor;
//    }
}
