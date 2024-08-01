package com.toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String profilePicture;
    private LocalDate accountCreationDate;
    private Set<Long> interestWebtoonIds;
    private Set<Long> interestAuthorIds;
    private Set<Long> interestGenreIds;
    private Set<Long> reviewIds;
}