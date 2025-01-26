package toonpick.app.unit.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toonpick.app.batch.AwsLambdaService;
import toonpick.app.batch.WebtoonItemProcessor;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.dto.webtoon.WebtoonUpdateResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class WebtoonItemProcessorTest {

    @Mock
    private AwsLambdaService awsLambdaService;

    @InjectMocks
    private WebtoonItemProcessor itemProcessor;

    @Test
    @DisplayName("Lambda에 요청을 성공적으로 전송하고 결과를 반환하는지 테스트")
    void testProcess() throws Exception {
        // given
        List<WebtoonUpdateRequest> mockRequests = List.of(new WebtoonUpdateRequest());
        List<WebtoonUpdateResult> mockResults = List.of(new WebtoonUpdateResult());
        when(awsLambdaService.invoke(mockRequests)).thenReturn(mockResults);

        // when
        List<WebtoonUpdateResult> result = itemProcessor.process(mockRequests);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(awsLambdaService, times(1)).invoke(mockRequests);
    }
}
