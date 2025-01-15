package toonpick.app.dto;

import lombok.Data;

import java.util.Set;

@Data
public class WebtoonResponse {

    private Long id;
    private String platform;
    private String title;
    private String publishDay;
    private String status;
    private String rating;
    private String thumbnailUrl;
    private String synopsis;
    private String link;
    private String ageRating;
    private Set<AuthorDTO> authors;
    private Set<String> genres;
    private int episodeCount;

}
