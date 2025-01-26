package toonpick.app.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import toonpick.app.dto.webtoon.WebtoonUpdateResult;
import toonpick.app.service.WebtoonService;

import java.util.List;


@Component
@RequiredArgsConstructor
public class WebtoonItemWriter implements ItemWriter<List<WebtoonUpdateResult>> {

    private final WebtoonService webtoonService;

    @Override
    public void write(Chunk<? extends List<WebtoonUpdateResult>> items) throws Exception {
        for (List<WebtoonUpdateResult> batch : items) {
            // 각 Batch 데이터를 웹툰 서비스로 전달하여 저장 처리

        }
    }
}
