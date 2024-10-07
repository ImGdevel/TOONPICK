package toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toonpick.app.entity.User;
import toonpick.app.entity.Webtoon;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonReviewDTO {
    private Long id;
    private String comment;
    private float rating;
    private User user;
    private Webtoon webtoon;
    private int likes;
    private LocalDate modifyDate;
}
