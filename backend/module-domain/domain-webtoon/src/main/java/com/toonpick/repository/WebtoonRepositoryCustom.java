package com.toonpick.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.toonpick.dto.WebtoonFilterDTO;
import com.toonpick.entity.Webtoon;


import java.util.List;

public interface WebtoonRepositoryCustom {
    Page<Webtoon> findWebtoonsByFilterOptions(WebtoonFilterDTO filter, Pageable pageable);
}
