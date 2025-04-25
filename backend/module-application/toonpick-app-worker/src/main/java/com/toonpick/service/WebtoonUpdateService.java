package com.toonpick.service;


import com.toonpick.dto.request.WebtoonUpdatePayload;
import com.toonpick.entity.Webtoon;
import com.toonpick.enums.SerializationStatus;
import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.publisher.WebtoonUpdatePublisher;
import com.toonpick.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebtoonUpdateService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonUpdatePublisher webtoonUpdatePublisher;
    private final WebtoonMapper webtoonMapper;

    private static final int BATCH_SIZE = 20;

    public void processWebtoonUpdate() {
        List<SerializationStatus> statuses = List.of(SerializationStatus.ONGOING, SerializationStatus.HIATUS);
        LocalDate thresholdDate = LocalDate.now().minusDays(6);

        List<Webtoon> webtoonsToUpdate = webtoonRepository.findWebtoonsForUpdate(statuses, thresholdDate);

        if (webtoonsToUpdate.isEmpty()) {
            return;
        }

        List<WebtoonUpdatePayload> payloads = webtoonsToUpdate.stream()
                .map(webtoonMapper::toWebtoonUpdatePayload)
                .toList();

        for (int i = 0; i < payloads.size(); i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, payloads.size());
            List<WebtoonUpdatePayload> batchPayload = payloads.subList(i, endIndex);
            webtoonUpdatePublisher.publishRequests(batchPayload);
        }
    }
}
