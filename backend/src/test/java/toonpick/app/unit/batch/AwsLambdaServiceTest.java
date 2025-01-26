package toonpick.app.unit.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import toonpick.app.batch.AwsLambdaService;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.dto.webtoon.WebtoonUpdateResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class AwsLambdaServiceTest {

    @Mock
    private LambdaClient lambdaClient;

    @InjectMocks
    private AwsLambdaService awsLambdaService;

    @Test
    @DisplayName("Lambda 요청이 성공적으로 수행되는지 테스트")
    void testInvoke() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        List<WebtoonUpdateRequest> mockRequests = List.of(new WebtoonUpdateRequest());
        String mockPayload = objectMapper.writeValueAsString(mockRequests);

        InvokeResponse mockResponse = InvokeResponse.builder()
                .payload(SdkBytes.fromUtf8String("[]"))
                .build();
        when(lambdaClient.invoke(any(InvokeRequest.class))).thenReturn(mockResponse);

        // when
        List<WebtoonUpdateResult> result = awsLambdaService.invoke(mockRequests);

        // then
        assertNotNull(result);
        verify(lambdaClient, times(1)).invoke(any(InvokeRequest.class));
    }
}
