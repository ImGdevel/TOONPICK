package com.toonpick.app.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class WebtoonDTO {

    private Long id;
    private String title;
    private String author;
    private String genre;
    private String description;

}
