package toonpick.app.webtoon.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WebtoonRequestDTO {

    private Long id;
    private Long uniqueId;
    private String platform;
    private String title;
    private String day;
    private String status;
    private Float rating;
    private String thumbnailUrl;
    private String story;
    private String url;
    private String ageRating;
    private List<AuthorRequest> authors;
    private List<String> genres;
    private int episodeCount;
    private String firstEpisodeLink;
    private LocalDate firstDay;
    private LocalDate lastUpdateDay;

    @Data
    public static class AuthorRequest {
        private String name;
        private String role;
        private String link;
    }
}
