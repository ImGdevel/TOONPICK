package toonpick.app.batch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.dto.webtoon.WebtoonUpdateResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AwsLambdaService {

    private final LambdaClient lambdaClient;

    private final String LAMBDA_FUNCTION = "your-lambda-function-name";

    /**
     * AWS Lambda로 데이터를 전송합니다.
     */
    public List<WebtoonUpdateResult> invoke(List<WebtoonUpdateRequest> items) {
        try {
            // JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String payload = objectMapper.writeValueAsString(items);

            // Lambda Invoke 요청 생성
            InvokeRequest invokeRequest = InvokeRequest.builder()
                    .functionName(LAMBDA_FUNCTION)
                    .payload(SdkBytes.fromUtf8String(payload))
                    .build();

            // Lambda 호출
            InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

            // 응답 Payload 처리
            String responsePayload = invokeResponse.payload().asUtf8String();
            return objectMapper.readValue(responsePayload, new TypeReference<List<WebtoonUpdateResult>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke AWS Lambda", e);
        }
    }

}
