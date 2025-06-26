package com.toonpick.mapper;

import com.toonpick.dto.payload.WebtoonCrawItem;
import com.toonpick.dto.payload.WebtoonEpisodeCrawItem;
import com.toonpick.domain.webtoon.entity.Platform;
import com.toonpick.domain.webtoon.entity.Webtoon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebtoonCrawItemMapper {

    @Mapping(target = "id", source = "webtoon.id")
    @Mapping(target = "platform", expression = "java(getFirstPlatformName(webtoon))")
    @Mapping(target = "url", expression = "java(getFirstPlatformLink(webtoon))")
    WebtoonCrawItem toWebtoonCrawItem(Webtoon webtoon);

    @Mapping(target = "id", source = "webtoon.id")
    @Mapping(target = "platform", expression = "java(getFirstPlatformName(webtoon))")
    @Mapping(target = "url", expression = "java(getFirstPlatformLink(webtoon))")
    @Mapping(target = "episodeCount", source = "webtoon.statistics.episodeCount")
    WebtoonEpisodeCrawItem toWebtoonEpisodeCrawItem(Webtoon webtoon);

    default String getFirstPlatformName(Webtoon webtoon) {
        if (webtoon.getPlatforms() == null || webtoon.getPlatforms().isEmpty()) {
            return null;
        }
        Platform platform = webtoon.getPlatforms().get(0).getPlatform();
        return platform != null ? platform.getName() : null;
    }

    default String getFirstPlatformLink(Webtoon webtoon) {
        if (webtoon.getPlatforms() == null || webtoon.getPlatforms().isEmpty()) {
            return null;
        }
        return webtoon.getPlatforms().get(0).getLink();
    }



}
