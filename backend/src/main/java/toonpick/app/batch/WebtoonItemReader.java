package toonpick.app.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;
import toonpick.app.mapper.WebtoonMapper;
import toonpick.app.repository.WebtoonRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WebtoonItemReader implements ItemReader<List<WebtoonUpdateRequest>> {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonMapper webtoonMapper;
    private int currentPage = 0;
    private final int pageSize = 100;

    @Override
    public List<WebtoonUpdateRequest> read() {
        // 오늘과 내일 날짜 계산
        LocalDate today = LocalDate.now();
        DayOfWeek tomorrow = today.plusDays(1).getDayOfWeek();

        // 페이징된 데이터 조회
        Pageable pageable = PageRequest.of(currentPage++, pageSize);
        List<Webtoon> webtoons = webtoonRepository.findWebtoonsForUpdate(today, tomorrow, pageable);

        // 빈 리스트가 반환되면 null 반환하여 종료 신호
        return webtoons.isEmpty() ? null : webtoons.stream()
                .map(webtoonMapper::webtoonToWebtoonUpdateRequest)
                .collect(Collectors.toList());
    }
}
