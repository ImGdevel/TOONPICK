package toonpick.app.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.dto.webtoon.WebtoonUpdateResult;

@Component
public class WebtoonItemProcessor implements ItemProcessor<WebtoonUpdateRequest, WebtoonUpdateResult> {

    @Override
    public WebtoonUpdateResult process(WebtoonUpdateRequest item) throws Exception {
        // todo :
        return null;
    }
}
