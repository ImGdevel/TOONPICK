package toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toonpick.app.dto.member.MemberProfileResponseDTO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebtoonReviewDTO {
    private Long id;
    private Long webtoonId;
    private MemberProfileResponseDTO memberId;
    private float rating;
    private String comment;
    private int likes;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
