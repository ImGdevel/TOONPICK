package com.toonpick.webtoon.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class WebtoonDetailsResponse extends WebtoonResponse {

    private List<WebtoonResponse> similarWebtoons;
    private WebtoonAnalysisData analysisData;

    public static class WebtoonAnalysisData {
        public int totalViews;
        public int totalSubscribers;
        public double averageViewTime;
        public double completionRate;
        public ReaderDemographics readerDemographics;
        public List<RatingDistribution> ratingDistribution;
        public ReviewSentiment reviewSentiment;
        public ContentAnalysis contentAnalysis;
        public GrowthMetrics growthMetrics;
        public List<PlatformComparison> platformComparison;
        public PredictionMetrics predictionMetrics;

        public static class ReaderDemographics {
            public List<AgeGroup> ageGroups;
            public List<GenderDistribution> genderDistribution;
            public List<RegionDistribution> regionDistribution;
        }

        public static class AgeGroup {
            public String age;
            public double percentage;
        }

        public static class GenderDistribution {
            public String gender;
            public double percentage;
        }

        public static class RegionDistribution {
            public String region;
            public double percentage;
        }

        public static class RatingDistribution {
            public int rating;
            public int count;
        }

        public static class ReviewSentiment {
            public int positive;
            public int neutral;
            public int negative;
        }

        public static class ContentAnalysis {
            public List<GenreDistribution> genreDistribution;
            public List<TagDistribution> tagDistribution;
            public List<CharacterPopularity> characterPopularity;
        }

        public static class GenreDistribution {
            public String genre;
            public double percentage;
        }

        public static class TagDistribution {
            public String tag;
            public int count;
        }

        public static class CharacterPopularity {
            public String character;
            public int popularity;
        }

        public static class GrowthMetrics {
            public List<DailyMetric> dailyViews;
            public List<DailyMetric> dailySubscribers;
            public List<DailyMetric> dailyComments;
        }

        public static class DailyMetric {
            public String date;
            public int count;
        }

        public static class PlatformComparison {
            public String platform;
            public double averageRating;
            public int totalViews;
        }

        public static class PredictionMetrics {
            public double expectedGrowth;
            public double retentionRate;
            public double churnRate;
        }
    }
}
