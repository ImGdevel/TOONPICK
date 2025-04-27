package com.toonpick.service;

import com.toonpick.dto.request.WebtoonCreateRequest;
import com.toonpick.dto.request.WebtoonUpdateRequest;
import com.toonpick.entity.Webtoon;
import com.toonpick.exception.ResourceAlreadyExistsException;
import com.toonpick.exception.ResourceNotFoundException;
import com.toonpick.mapper.WebtoonMapper;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonMapper webtoonMapper;

    /**
     * 새로운 웹툰 추가 (이미 존재하는 경우 거부)
     */
    public void createWebtoon(WebtoonCreateRequest webtoonCreateRequest) {
        if(webtoonRepository.existsByExternalId(webtoonCreateRequest.getPlatform() + webtoonCreateRequest.getExternalId())){
            throw new ResourceAlreadyExistsException(ErrorCode.WEBTOON_ALREADY_EXISTS);
        }
        Webtoon webtoon = webtoonMapper.toWebtoon(webtoonCreateRequest);
        webtoonRepository.save(webtoon);
    }

    /**
     * 기존 웹툰 데이터 업데이트
     */
    public void updateWebtoon(WebtoonUpdateRequest webtoonUpdateRequest) {
        Webtoon webtoon = webtoonRepository.findById(webtoonUpdateRequest.getId())
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND));

        // todo : 업데이트 가능한 데이터만 업데이트
        // webtoon.updateWebtoonDetails();

        webtoonRepository.save(webtoon);
    }

    /**
     * 웹툰 제거
     */
    public void deleteWebtoon(Long id) {
        if (!webtoonRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND);
        }
        webtoonRepository.deleteById(id);
    }
}
