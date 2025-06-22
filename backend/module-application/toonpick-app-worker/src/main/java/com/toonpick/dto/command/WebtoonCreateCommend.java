package com.toonpick.dto.command;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.toonpick.enums.AgeRating;
import com.toonpick.enums.AuthorRole;
import com.toonpick.enums.SerializationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebtoonCreateCommend {

    @JsonProperty("id")
    private String titleId;

    @JsonProperty("url")
    private String url;

    @JsonProperty("title")
    private String title;

    @JsonProperty("uniqueId")
    private String uniqueId;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("description")
    private String description;

    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;

    @JsonProperty("dayOfWeek")
    private String dayOfWeek;

    @JsonProperty("status")
    private SerializationStatus status;

    @JsonProperty("ageRating")
    private AgeRating ageRating;

    @JsonProperty("episodeCount")
    private Integer episodeCount;

    @JsonProperty("previewCount")
    private Integer previewCount;

    @JsonProperty("genres")
    private List<String> genres;

    @JsonProperty("authors")
    private List<AuthorRequest> authors;

    @JsonProperty("publishStartDate")
    private String publishStartDate;

    @JsonProperty("lastUpdatedDate")
    private String lastUpdatedDate;

    @JsonProperty("episodes")
    private List<EpisodeInfo> episodes;

    @JsonProperty("relatedNovels")
    private List<RelatedNovelRequest> relatedNovels;

    @JsonProperty("relatedWebtoonIds")
    private List<String> relatedWebtoonIds;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthorRequest {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("role")
        private String role;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RelatedNovelRequest {

        @JsonProperty("title")
        private String title;

        @JsonProperty("link")
        private String link;

        @JsonProperty("thumbnailUrl")
        private String thumbnailUrl;

        @JsonProperty("type")
        private String type;

        @JsonProperty("freeEpisodeCount")
        private Integer freeEpisodeCount;
    }
}