package com.submarket.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 기본 실행 대기하는 Thread 의 수
        executor.setMaxPoolSize(30); // 동시 동작하는 최대 Thread의 수
        executor.setQueueCapacity(50); // 초과 Thread 생성 요청 시 Queue 로 저장함, 이때 수용 가능한 수
        executor.setThreadNamePrefix("Thread-00000"); // 생성되는 Thread 접두사
        executor.initialize();

        return executor;
    }
}