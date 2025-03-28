package toonpick.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.domain.webtoon.enums.SerializationStatus;
import toonpick.app.dto.webtoon.WebtoonUpdateBatchRequestDTO;
import toonpick.app.dto.webtoon.WebtoonUpdateRequestDTO;
import toonpick.app.mapper.WebtoonMapper;
import toonpick.app.repository.WebtoonRepository;
import toonpick.app.sqs.WebtoonSqsSender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebtoonUpdateService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonSqsSender webtoonSqsSender;
    private final WebtoonMapper webtoonMapper;

    private static final int BATCH_SIZE = 1000;

    @Transactional
    public int sendWebtoonsForUpdate() {
        LocalDate thresholdDate = LocalDate.now().minusDays(6);
        List<Webtoon> webtoons = webtoonRepository.findWebtoonsForUpdate(
            List.of(SerializationStatus.ONGOING, SerializationStatus.HIATUS),
            thresholdDate
        );

        if (webtoons.isEmpty()) {
            log.info("No webtoons found for update.");
            return 0;
        }

        // 1000개 단위로 SQS 메시지 전송
        List<List<Webtoon>> partitions = partitionList(webtoons, BATCH_SIZE);

        int totalSent = 0;

        for (List<Webtoon> batch : partitions) {
            List<WebtoonUpdateRequestDTO> requests = batch.stream()
                .map(webtoonMapper::webtoonToWebtoonUpdateRequestDTO)
                .toList();

            webtoonSqsSender.sendMessages(requests);
            totalSent += requests.size();
        }

        log.info("Total webtoons sent for update: {}", totalSent);
        return totalSent;
    }

    // 배치 작업을 나누는 리스트
    public static <T> List<List<T>> partitionList(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }

}
