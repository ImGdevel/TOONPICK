package toonpick.app.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.repository.WebtoonRepository;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final WebtoonRepository webtoonRepository;

    @Bean
    public Job webtoonUpdate(Step processWebtoonsStep, Step retryFailedWebtoonsStep){
        return new JobBuilder("webtoonUpdateJob", jobRepository)
                .start(processWebtoonsStep)
                .next(retryFailedWebtoonsStep)
                .build();
    }

    @Bean
    public Step processWebtoonsStep(
            ItemReader<WebtoonUpdateRequest> itemReader,
            ItemProcessor<WebtoonUpdateRequest, Webtoon> itemProcessor,
            ItemWriter<Webtoon> itemWriter
    ){

        return new StepBuilder("processWebtoonsStep", jobRepository)
                .<WebtoonUpdateRequest, Webtoon>chunk(10, platformTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Step retryFailedWebtoonsStep(
            ItemReader<WebtoonUpdateRequest> itemReader,
            ItemProcessor<WebtoonUpdateRequest, Webtoon> itemProcessor,
            ItemWriter<Webtoon> itemWriter
    ){

        return new StepBuilder("retryFailedWebtoonsStep", jobRepository)
                .<WebtoonUpdateRequest, Webtoon>chunk(10, platformTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public ItemReader<WebtoonUpdateRequest> webtoonItemReader() {
        return new WebtoonItemReader(webtoonRepository);
    }
}
