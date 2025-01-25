package toonpick.app.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import toonpick.app.dto.webtoon.WebtoonUpdateResult;
import toonpick.app.repository.WebtoonRepository;


@Component
@RequiredArgsConstructor
public class WebtoonItemWriter implements ItemWriter<WebtoonUpdateResult> {

    private final WebtoonRepository webtoonRepository;

    @Override
    public void write(Chunk<? extends WebtoonUpdateResult> chunk) throws Exception {
        // todo : 결과 저장 로직
    }
}
