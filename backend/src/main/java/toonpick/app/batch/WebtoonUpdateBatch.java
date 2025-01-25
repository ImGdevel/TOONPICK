package toonpick.app.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.dto.webtoon.WebtoonUpdateResult;

@Configuration
@RequiredArgsConstructor
public class WebtoonUpdateBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final WebtoonItemReader itemReader;
    private final WebtoonItemProcessor itemProcessor;
    private final WebtoonItemWriter itemWriter;

    @Bean
    public Job webtoonUpdate(Step processWebtoonsStep, Step retryFailedWebtoonsStep){
        return new JobBuilder("webtoonUpdateJob", jobRepository)
                .start(processWebtoonsStep)
                //.next(retryFailedWebtoonsStep) todo : 실패에 대한 재시도 작업은 일단 보류 한다.
                .build();
    }

    @Bean
    public Step processWebtoonsStep(){

        return new StepBuilder("processWebtoonsStep", jobRepository)
                .<WebtoonUpdateRequest, WebtoonUpdateResult>chunk(10, platformTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    // todo : 실패에 대한 재시도 작업은 일단 보류 한다.
    @Bean
    public Step retryFailedWebtoonsStep(){

        return new StepBuilder("retryFailedWebtoonsStep", jobRepository)
                .<WebtoonUpdateRequest, WebtoonUpdateResult>chunk(10, platformTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

}
