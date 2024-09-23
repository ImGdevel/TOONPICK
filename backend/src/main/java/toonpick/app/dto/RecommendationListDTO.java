package toonpick.app.dto;

import lombok.Data;
import toonpick.app.entity.RecommendationList;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RecommendationListDTO {
    private Long id;
    private String theme;
    private String description;
    private List<Long> webtoonIds;
}
