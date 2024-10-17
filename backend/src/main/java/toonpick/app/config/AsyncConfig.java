package toonpick.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "likeUpdateExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);    // 기본 쓰레드
        executor.setMaxPoolSize(10);    // 최대 쓰레드
        executor.setQueueCapacity(25);  // 큐의 큐기
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }
}
