package toonpick.app.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toonpick.app.dto.MemberDTO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonReviewDTO {
    private Long id;
    private Long webtoonId;
    private MemberDTO memberId;
    private float rating;
    private String comment;
    private int likes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
