package toonpick.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestWebtoonDTO {
    private Long id;
    private UserDTO user;
    private WebtoonDTO webtoon;
}