package toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRatingDTO {
    private Long id;
    private UserDTO user;
    private WebtoonDTO webtoon;
    private LocalDate reviewDate;
    private float rating;
    private String comment;
    private int likes;

}
