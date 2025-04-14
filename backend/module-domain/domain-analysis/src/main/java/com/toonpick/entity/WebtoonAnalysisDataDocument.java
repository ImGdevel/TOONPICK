package com.toonpick.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "webtoon_analysis")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebtoonAnalysisDataDocument {

    @Id
    private String id;

    private Long webtoonId;

    private int totalViews;
    private int totalSubscribers;
    private double averageViewTime;
    private double completionRate;

    private ReaderDemographics readerDemographics;
    private List<RatingDistribution> ratingDistribution;
    private ReviewSentiment reviewSentiment;
    private ContentAnalysis contentAnalysis;
    private GrowthMetrics growthMetrics;
    private List<PlatformComparison> platformComparison;
    private PredictionMetrics predictionMetrics;

    // Embedded Classes
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReaderDemographics {
        private List<AgeGroup> ageGroups;
        private List<GenderDistribution> genderDistribution;
        private List<RegionDistribution> regionDistribution;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AgeGroup {
        private String age;
        private double percentage;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GenderDistribution {
        private String gender;
        private double percentage;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegionDistribution {
        private String region;
        private double percentage;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RatingDistribution {
        private int rating;
        private int count;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewSentiment {
        private int positive;
        private int neutral;
        private int negative;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentAnalysis {
        private List<GenreDistribution> genreDistribution;
        private List<TagDistribution> tagDistribution;
        private List<CharacterPopularity> characterPopularity;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GenreDistribution {
        private String genre;
        private double percentage;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TagDistribution {
        private String tag;
        private int count;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CharacterPopularity {
        private String character;
        private int popularity;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GrowthMetrics {
        private List<DailyMetric> dailyViews;
        private List<DailyMetric> dailySubscribers;
        private List<DailyMetric> dailyComments;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyMetric {
        private String date;
        private int count;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlatformComparison {
        private String platform;
        private double averageRating;
        private int totalViews;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PredictionMetrics {
        private double expectedGrowth;
        private double retentionRate;
        private double churnRate;
    }
}
