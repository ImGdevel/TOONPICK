package toonpick.app.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private int currentPage = 0;
    private final int pageSize = 100;
    private List<WebtoonUpdateRequest> currentBatch;
    private Iterator<WebtoonUpdateRequest> iterator;

    @Override
    public WebtoonUpdateRequest read() {
        if (iterator == null || !iterator.hasNext()) {
            fetchNextBatch();
        }
        return (iterator != null && iterator.hasNext()) ? iterator.next() : null;
    }

    private void fetchNextBatch() {
        LocalDate today = LocalDate.now();
        DayOfWeek tomorrow = today.plusDays(1).getDayOfWeek();

        Pageable pageable = PageRequest.of(currentPage++, pageSize);
        currentBatch = webtoonRepository.findWebtoonsForUpdate(today, tomorrow, pageable);

        if (currentBatch != null && !currentBatch.isEmpty()) {
            iterator = currentBatch.iterator();
        } else {
            iterator = null;
        }
    }
}
