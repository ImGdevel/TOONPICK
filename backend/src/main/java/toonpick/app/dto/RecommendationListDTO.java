package toonpick.app.dto;

import lombok.Data;

import java.util.List;


@Data
public class RecommendationListDTO {
    private Long id;
    private String theme;
    private String description;
    private List<Long> webtoonIds;
}
