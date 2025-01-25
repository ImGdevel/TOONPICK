package toonpick.app.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import toonpick.app.repository.WebtoonRepository;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final WebtoonRepository webtoonRepository;

    @Bean
    public Job webtoonUpdate(Step webtoonUpdateStep, Step retryFailedStep){
        return new JobBuilder("webtoonUpdateJob", jobRepository)
                .start(webtoonUpdateStep)
                .next(retryFailedStep)
                .build();
    }

}
