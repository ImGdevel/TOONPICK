package toonpick.app.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.dto.webtoon.WebtoonUpdateResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebtoonItemProcessor implements ItemProcessor<List<WebtoonUpdateRequest>, List<WebtoonUpdateResult>> {

    private final AwsLambdaService awsLambdaService;

    @Override
    public List<WebtoonUpdateResult> process(List<WebtoonUpdateRequest> items) throws Exception {
        // 100개의 데이터(batch)를 Lambda로 전송
        return awsLambdaService.invoke(items);
    }
}
