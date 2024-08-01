package com.toonpick.app.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonDTO {
    private Long id;
    private String title;
    private Long authorId;
    private float rating;
    private String description;
    private String imageUrl;
    private Set<Long> genreIds;
}