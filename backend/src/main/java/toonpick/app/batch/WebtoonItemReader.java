package toonpick.app.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.repository.WebtoonRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WebtoonItemReader implements ItemReader<WebtoonUpdateRequest> {

    private final WebtoonRepository webtoonRepository;
    private Iterator<WebtoonUpdateRequest> iterator;

    @Override
    public WebtoonUpdateRequest read() {
        LocalDate today = LocalDate.now();
            DayOfWeek tomorrow = today.plusDays(1).getDayOfWeek();
        if (iterator == null) {
            List<WebtoonUpdateRequest> webtoons = webtoonRepository.findWebtoonsForUpdate(today, tomorrow);
            iterator = webtoons.iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }
}
