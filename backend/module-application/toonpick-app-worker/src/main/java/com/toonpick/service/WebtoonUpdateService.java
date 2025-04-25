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

        webtoonUpdatePublisher.publishRequests(payloads);
    }
}
