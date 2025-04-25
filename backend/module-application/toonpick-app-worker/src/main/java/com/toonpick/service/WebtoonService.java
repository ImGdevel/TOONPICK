package com.toonpick.service;

import com.toonpick.dto.request.CreateWebtoonRequest;
import com.toonpick.dto.request.UpdateWebtoonRequest;
import com.toonpick.entity.Webtoon;
import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;

    private final WebtoonMapper webtoonMapper;

    /**
     * 웹툰이 존재하지 않는 경우에만 새로 생성
     */
    public void createWebtoon(CreateWebtoonRequest request) {
        if (webtoonRepository.existsById(request.getId())) {
            return;
        }

        // todo : 추가적인 생성 로직 작성

        Webtoon webtoon = webtoonMapper.toWebtoon(request);
        webtoonRepository.save(webtoon);
    }

    /**
     * 기존 웹툰과 변경사항이 있는 경우에만 업데이트
     */
    public boolean updateWebtoon(UpdateWebtoonRequest request) {
        Webtoon webtoon = webtoonRepository.findById(request.getId())
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.WEBTOON_NOT_FOUND.getMessage()));

        webtoon.getLastUpdatedDate();

        // todo : 추가 업데이트 로직 작성

        webtoonRepository.save(webtoon);
        return true;
    }
}
