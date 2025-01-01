package toonpick.app.webtoon.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import toonpick.app.webtoon.dto.WebtoonFilterDTO;
import toonpick.app.webtoon.entity.QWebtoon;
import toonpick.app.webtoon.entity.Webtoon;
import toonpick.app.webtoon.entity.enums.AgeRating;
import toonpick.app.webtoon.entity.enums.Platform;
import toonpick.app.webtoon.entity.enums.SerializationStatus;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class WebtoonRepositoryImpl implements WebtoonRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Webtoon> findWebtoonsByFilter(WebtoonFilterDTO filter) {
        QWebtoon webtoon = QWebtoon.webtoon;

        return queryFactory.selectFrom(webtoon)
                .where(
                        platformEq(filter.getPlatform()),
                        serializationStatusEq(filter.getSerializationStatus()),
                        ageRatingEq(filter.getAgeRating()),
                        weekEq(filter.getWeek()),
                        genresIn(filter.getGenres()),
                        authorsIn(filter.getAuthors())
                )
                .fetch();
    }

    @Override
    public Page<Webtoon> findWebtoonsByFilterOptions(WebtoonFilterDTO filter, Pageable pageable) {
        QWebtoon webtoon = QWebtoon.webtoon;

        JPAQuery<Webtoon> query = queryFactory.selectFrom(webtoon)
                .where(
                        platformEq(filter.getPlatform()),
                        serializationStatusEq(filter.getSerializationStatus()),
                        ageRatingEq(filter.getAgeRating()),
                        weekEq(filter.getWeek()),
                        genresIn(filter.getGenres()),
                        authorsIn(filter.getAuthors())
                );

        long total = query.fetchCount();
        List<Webtoon> content = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression platformEq(Platform platform) {
        return platform != null ? QWebtoon.webtoon.platform.eq(platform) : null;
    }

    private BooleanExpression serializationStatusEq(SerializationStatus serializationStatus) {
        return serializationStatus != null ? QWebtoon.webtoon.serializationStatus.eq(serializationStatus) : null;
    }

    private BooleanExpression ageRatingEq(AgeRating ageRating) {
        return ageRating != null ? QWebtoon.webtoon.ageRating.eq(ageRating) : null;
    }

    private BooleanExpression weekEq(java.time.DayOfWeek week) {
        return week != null ? QWebtoon.webtoon.week.eq(week) : null;
    }

    private BooleanExpression genresIn(Set<String> genres) {
        return genres != null && !genres.isEmpty() ? QWebtoon.webtoon.genres.any().name.in(genres) : null;
    }

    private BooleanExpression authorsIn(Set<String> authors) {
        return authors != null && !authors.isEmpty() ? QWebtoon.webtoon.authors.any().name.in(authors) : null;
    }
}
