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
    private String password;
    private String role;
    private String name;
    private String profilePicture;
    private LocalDate accountCreationDate;
    private Set<InterestWebtoonDTO> interestWebtoons;
    private Set<InterestAuthorDTO> interestAuthors;
    private Set<InterestGenreDTO> interestGenres;
    private Set<ReviewDTO> reviews;

}