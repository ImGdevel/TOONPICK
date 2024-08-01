package com.toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestGenreDTO {
    private Long id;
    private Long userId;
    private Long genreId;
}