package com.toonpick.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toonpick.entity.QWebtoon;
import com.toonpick.entity.QWebtoonPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.toonpick.dto.WebtoonFilterDTO;
import com.toonpick.entity.Webtoon;
import com.toonpick.enums.AgeRating;
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
        QWebtoonPlatform webtoonPlatform = QWebtoonPlatform.webtoonPlatform;

        JPAQuery<Webtoon> query = queryFactory.selectFrom(webtoon)
            .distinct()
            .leftJoin(webtoon.platforms, webtoonPlatform)
            .where(
                platformsIn(filter.getPlatforms(), webtoonPlatform),
                serializationStatusesIn(filter.getSerializationStatuses(), webtoon),
                ageRatingsIn(filter.getAgeRatings(), webtoon),
                publishDaysIn(filter.getPublishDays(), webtoon),
                genresIn(filter.getGenres(), webtoon),
                authorsIn(filter.getAuthors(), webtoon)
            );

        long total = query.fetchCount();

        List<Webtoon> content = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression platformsIn(Set<String> platforms, QWebtoonPlatform webtoonPlatform) {
        return (platforms != null && !platforms.isEmpty()) ?
            webtoonPlatform.platform.name.in(platforms) : null;
    }

    private BooleanExpression serializationStatusesIn(Set<SerializationStatus> statuses, QWebtoon webtoon) {
        return (statuses != null && !statuses.isEmpty()) ?
            webtoon.serializationStatus.in(statuses) : null;
    }

    private BooleanExpression ageRatingsIn(Set<AgeRating> ratings, QWebtoon webtoon) {
        return (ratings != null && !ratings.isEmpty()) ?
            webtoon.ageRating.in(ratings) : null;
    }

    private BooleanExpression publishDaysIn(Set<DayOfWeek> days, QWebtoon webtoon) {
        return (days != null && !days.isEmpty()) ?
            webtoon.dayOfWeek.in(days) : null;
    }

    private BooleanExpression genresIn(Set<String> genres, QWebtoon webtoon) {
        return (genres != null && !genres.isEmpty()) ?
            webtoon.genres.any().name.in(genres) : null;
    }

    private BooleanExpression authorsIn(Set<String> authors, QWebtoon webtoon) {
        return (authors != null && !authors.isEmpty()) ?
            webtoon.authors.any().name.in(authors) : null;
    }
}
