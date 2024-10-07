package toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toonpick.app.entity.User;
import toonpick.app.entity.Webtoon;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonReviewDTO {
    private Long id;
    private Long userId;
    private Long webtoonId;
    private float rating;
    private String comment;
    private int likes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
