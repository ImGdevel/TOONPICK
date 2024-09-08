package toonpick.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class WebtoonRequestDTO {

    private Long id;
    private String uniqueId;
    private String platform;
    private String title;
    private String day;
    private String status;
    private String rating;
    private String thumbnailUrl;
    private String story;
    private String url;
    private String ageRating;
    private List<AuthorRequest> authors;
    private List<String> genres;
    private int episodeCount;

    @Data
    public static class AuthorRequest {
        private String name;
        private String role;
        private String link;
    }
}
