package com.toonpick.service;

import com.toonpick.dto.result.WebtoonEpisodeUpdateResult;
import com.toonpick.repository.WebtoonEpisodeRepository;
import com.toonpick.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebtoonEpisodeUpdateService {

    private final WebtoonEpisodeRepository webtoonEpisodeRepository;

    private final WebtoonRepository webtoonRepository;

    /**
     * 샤로운 에피소드 등록
     */
    public void createNewEpisode(WebtoonEpisodeUpdateResult webtoonEpisodeUpdateResult){
        // todo : 웹툰 새로운 에피소드 등록
    }

}
