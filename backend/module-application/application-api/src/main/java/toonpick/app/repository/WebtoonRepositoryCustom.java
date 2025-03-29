package toonpick.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import toonpick.app.dto.WebtoonFilterDTO;
import toonpick.app.domain.webtoon.Webtoon;

import java.util.List;

public interface WebtoonRepositoryCustom {
    List<Webtoon> findWebtoonsByFilter(WebtoonFilterDTO filter);
    Page<Webtoon> findWebtoonsByFilterOptions(WebtoonFilterDTO filter, Pageable pageable);
}
