package com.toonpick.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toonpick.entity.QWebtoon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.toonpick.dto.WebtoonFilterDTO;
import com.toonpick.entity.Webtoon;
import com.toonpick.enums.AgeRating;
import com.toonpick.enums.Platform;
import com.toonpick.enums.SerializationStatus;


import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class WebtoonRepositoryImpl implements WebtoonRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Webtoon> findWebtoonsByFilterOptions(WebtoonFilterDTO filter, Pageable pageable) {
        QWebtoon webtoon = QWebtoon.webtoon;

        JPAQuery<Webtoon> query = queryFactory.selectFrom(webtoon)
            .where(
                platformsIn(filter.getPlatforms()),
                serializationStatusesIn(filter.getSerializationStatuses()),
                ageRatingsIn(filter.getAgeRatings()),
                publishDaysIn(filter.getPublishDays()),
                genresIn(filter.getGenres()),
                authorsIn(filter.getAuthors())
            );

        long total = query.fetchCount();

        List<Webtoon> content = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression platformsIn(Set<Platform> platforms) {
        return (platforms != null && !platforms.isEmpty()) ? QWebtoon.webtoon.platform.in(platforms) : null;
    }

    private BooleanExpression serializationStatusesIn(Set<SerializationStatus> statuses) {
        return (statuses != null && !statuses.isEmpty()) ? QWebtoon.webtoon.serializationStatus.in(statuses) : null;
    }

    private BooleanExpression ageRatingsIn(Set<AgeRating> ratings) {
        return (ratings != null && !ratings.isEmpty()) ? QWebtoon.webtoon.ageRating.in(ratings) : null;
    }

    private BooleanExpression publishDaysIn(Set<DayOfWeek> days) {
        return (days != null && !days.isEmpty()) ? QWebtoon.webtoon.dayOfWeek.in(days) : null;
    }

    private BooleanExpression genresIn(Set<String> genres) {
        return (genres != null && !genres.isEmpty()) ? QWebtoon.webtoon.genres.any().name.in(genres) : null;
    }

    private BooleanExpression authorsIn(Set<String> authors) {
        return (authors != null && !authors.isEmpty()) ? QWebtoon.webtoon.authors.any().name.in(authors) : null;
    }

}
