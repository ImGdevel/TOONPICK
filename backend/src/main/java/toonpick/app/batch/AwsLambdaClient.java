package toonpick.app.batch;

import org.springframework.stereotype.Component;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.dto.webtoon.WebtoonUpdateResult;

import java.util.List;

@Component
public class AwsLambdaClient {

    public List<WebtoonUpdateResult> invoke(List<WebtoonUpdateRequest> items) {

        // todo : 추후 구현

        return new List<WebtoonUpdateResult>;
    }

}
