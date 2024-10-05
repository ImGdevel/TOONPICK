package toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toonpick.app.entity.User;
import toonpick.app.entity.Webtoon;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRatingDTO {
    private Long id;
    private String comment;
    private Integer rating;
    private User user;
    private Webtoon webtoon;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
