package toonpick.app.unit.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.transaction.PlatformTransactionManager;
import toonpick.app.batch.WebtoonItemProcessor;
import toonpick.app.batch.WebtoonItemReader;
import toonpick.app.batch.WebtoonItemWriter;
import toonpick.app.batch.WebtoonUpdateBatch;

import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class WebtoonUpdateBatchTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private PlatformTransactionManager platformTransactionManager;

    @Mock
    private WebtoonItemReader itemReader;

    @Mock
    private WebtoonItemProcessor itemProcessor;

    @Mock
    private WebtoonItemWriter itemWriter;

    @InjectMocks
    private WebtoonUpdateBatch webtoonUpdateBatch;

    @Test
    @DisplayName("WebtoonUpdate Job이 성공적으로 생성되는지 테스트")
    void testWebtoonUpdateJobCreation() {
        // when
        Step step = webtoonUpdateBatch.processWebtoonsStep();
        Job job = webtoonUpdateBatch.webtoonUpdate(step);

        // then
        assertNotNull(job);
        assertEquals("webtoonUpdateJob", job.getName());
    }

    @Test
    @DisplayName("ProcessWebtoons Step이 성공적으로 생성되는지 테스트")
    void testProcessWebtoonsStepCreation() {
        // when
        Step step = webtoonUpdateBatch.processWebtoonsStep();

        // then
        assertNotNull(step);
        assertEquals("processWebtoonsStep", step.getName());
    }
}
