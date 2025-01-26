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

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebtoonUpdateBatch {

    private static final String JOB_NAME = "webtoonUpdateJob";
    private static final String STEP_NAME = "processWebtoonsStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final WebtoonItemReader itemReader;
    private final WebtoonItemProcessor itemProcessor;
    private final WebtoonItemWriter itemWriter;

    @Bean
    public Job webtoonUpdate(Step processWebtoonsStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(processWebtoonsStep)
                .build();
    }

    @Bean
    public Step processWebtoonsStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<List<WebtoonUpdateRequest>, List<WebtoonUpdateResult>>chunk(1, platformTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }
}
