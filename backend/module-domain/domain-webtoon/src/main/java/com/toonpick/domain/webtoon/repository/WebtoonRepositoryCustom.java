package com.toonpick.domain.webtoon.repository;

import com.toonpick.domain.webtoon.dto.WebtoonFilterDTO;
import com.toonpick.domain.webtoon.entity.Webtoon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WebtoonRepositoryCustom {
    Page<Webtoon> findWebtoonsByFilterOptions(WebtoonFilterDTO filter, Pageable pageable);
}
