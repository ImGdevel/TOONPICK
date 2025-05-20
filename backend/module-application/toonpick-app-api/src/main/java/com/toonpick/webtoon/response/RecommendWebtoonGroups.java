package com.toonpick.webtoon.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendWebtoonGroups {

    private String title;

    private String description;

    List<WebtoonResponse> webtoonItems;

    int totalItemSize;
}
