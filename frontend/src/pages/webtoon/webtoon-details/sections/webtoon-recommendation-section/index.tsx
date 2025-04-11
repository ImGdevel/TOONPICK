import React from 'react';
import { Webtoon, Platform, SerializationStatus } from '@models/webtoon';
import styles from './style.module.css';
import WebtoonCard from '@components/webtoon-card';
import WebtoonList from '@components/webtoon-list';

interface WebtoonRecommendationSectionProps {
  webtoon: Webtoon;
}

const WebtoonRecommendationSection: React.FC<WebtoonRecommendationSectionProps> = ({ webtoon }) => {
  // 더미 데이터 (Webtoon 인터페이스에 맞춰 수정)
  const recommendations: {
    similarWebtoons: Webtoon[];
    userSimilarWebtoons: Webtoon[];
    sameAuthorWebtoons: Webtoon[];
    randomWebtoons: Webtoon[];
  } = {
    similarWebtoons: [
      {
        id: 1,
        title: '유사한 웹툰 1',
        thumbnailUrl: 'https://via.placeholder.com/150',
        platform: Platform.NAVER,
        status: SerializationStatus.ONGOING,
        isAdult: false,
        publishDay: '월',
        authors: [{ id: 1, name: '작가1' }],
        description: '유사한 웹툰 1의 설명입니다.',
        genres: [{ id: 1, name: '판타지' }],
        totalRatings: 1000,
        averageRating: 4.5,
        analysisData: null,
        similarWebtoons: []
      },
      {
        id: 4,
        title: '랜덤 추천 웹툰 1',
        thumbnailUrl: 'https://via.placeholder.com/150',
        platform: Platform.KAKAOPAGE,
        status: SerializationStatus.ONGOING,
        isAdult: false,
        publishDay: '목',
        authors: [{ id: 3, name: '작가3' }],
        description: '랜덤 추천 웹툰 1의 설명입니다.',
        genres: [{ id: 1, name: '판타지' }],
        totalRatings: 900,
        averageRating: 4.2,
        analysisData: null,
        similarWebtoons: []
      },
      {
        id: 4,
        title: '랜덤 추천 웹툰 1',
        thumbnailUrl: 'https://via.placeholder.com/150',
        platform: Platform.KAKAOPAGE,
        status: SerializationStatus.ONGOING,
        isAdult: false,
        publishDay: '목',
        authors: [{ id: 3, name: '작가3' }],
        description: '랜덤 추천 웹툰 1의 설명입니다.',
        genres: [{ id: 1, name: '판타지' }],
        totalRatings: 900,
        averageRating: 4.2,
        analysisData: null,
        similarWebtoons: []
      },
      {
        id: 4,
        title: '랜덤 추천 웹툰 1',
        thumbnailUrl: 'https://via.placeholder.com/150',
        platform: Platform.KAKAOPAGE,
        status: SerializationStatus.ONGOING,
        isAdult: false,
        publishDay: '목',
        authors: [{ id: 3, name: '작가3' }],
        description: '랜덤 추천 웹툰 1의 설명입니다.',
        genres: [{ id: 1, name: '판타지' }],
        totalRatings: 900,
        averageRating: 4.2,
        analysisData: null,
        similarWebtoons: []
      },
      {
        id: 4,
        title: '랜덤 추천 웹툰 1',
        thumbnailUrl: 'https://via.placeholder.com/150',
        platform: Platform.KAKAOPAGE,
        status: SerializationStatus.ONGOING,
        isAdult: false,
        publishDay: '목',
        authors: [{ id: 3, name: '작가3' }],
        description: '랜덤 추천 웹툰 1의 설명입니다.',
        genres: [{ id: 1, name: '판타지' }],
        totalRatings: 900,
        averageRating: 4.2,
        analysisData: null,
        similarWebtoons: []
      },
      {
        id: 4,
        title: '랜덤 추천 웹툰 1',
        thumbnailUrl: 'https://via.placeholder.com/150',
        platform: Platform.KAKAOPAGE,
        status: SerializationStatus.ONGOING,
        isAdult: false,
        publishDay: '목',
        authors: [{ id: 3, name: '작가3' }],
        description: '랜덤 추천 웹툰 1의 설명입니다.',
        genres: [{ id: 1, name: '판타지' }],
        totalRatings: 900,
        averageRating: 4.2,
        analysisData: null,
        similarWebtoons: []
      }
      // ... 더 많은 유사 웹툰
    ],
    userSimilarWebtoons: [
      {
        id: 2,
        title: '유저 유사 웹툰 1',
        thumbnailUrl: 'https://via.placeholder.com/150',
        platform: Platform.KAKAO,
        status: SerializationStatus.ONGOING,
        isAdult: false,
        publishDay: '화',
        authors: [{ id: 2, name: '작가2' }],
        description: '유저 유사 웹툰 1의 설명입니다.',
        genres: [{ id: 1, name: '판타지' }],
        totalRatings: 800,
        averageRating: 4.3,
        analysisData: null,
        similarWebtoons: []
      },
    ],
    sameAuthorWebtoons: [
      {
        id: 3,
        title: '같은 작가 웹툰 1',
        thumbnailUrl: 'https://via.placeholder.com/150',
        platform: Platform.NAVER,
        status: SerializationStatus.ONGOING,
        isAdult: false,
        publishDay: '수',
        authors: webtoon.authors, // 현재 웹툰의 작가와 동일
        description: '같은 작가 웹툰 1의 설명입니다.',
        genres: [{ id: 1, name: '판타지' }],
        totalRatings: 1200,
        averageRating: 4.7,
        analysisData: null,
        similarWebtoons: []
      }
    ],
    randomWebtoons: [
      {
        id: 4,
        title: '랜덤 추천 웹툰 1',
        thumbnailUrl: 'https://via.placeholder.com/150',
        platform: Platform.KAKAOPAGE,
        status: SerializationStatus.ONGOING,
        isAdult: false,
        publishDay: '목',
        authors: [{ id: 3, name: '작가3' }],
        description: '랜덤 추천 웹툰 1의 설명입니다.',
        genres: [{ id: 1, name: '판타지' }],
        totalRatings: 900,
        averageRating: 4.2,
        analysisData: null,
        similarWebtoons: []
      },
      // ... 더 많은 랜덤 웹툰
    ]
  };

  return (
    <section className={styles.section}>
      <h2 className={styles.sectionTitle}>추천 웹툰</h2>

      {/* 통합 유사도 웹툰 */}
      <div className={styles.recommendationSection}>
        <h3 className={styles.subtitle}>이 웹툰과 비슷한 웹툰</h3>
        <WebtoonList webtoons={recommendations.sameAuthorWebtoons} />
      </div>

      {/* 유저 유사도 웹툰 */}
      <div className={styles.recommendationSection}>
        <h3 className={styles.subtitle}>이 웹툰을 좋아하는 사람들이 함께 본 웹툰</h3>
        <WebtoonList webtoons={recommendations.userSimilarWebtoons} />
      </div>

      {/* 동일 작가 웹툰 */}
      <div className={styles.recommendationSection}>
        <h3 className={styles.subtitle}>같은 작가의 다른 웹툰</h3>
        <WebtoonList webtoons={recommendations.similarWebtoons} />
      </div>

      {/* 랜덤 추천 웹툰 */}
      <div className={styles.recommendationSection}>
        <h3 className={styles.subtitle}>이런 웹툰은 어떠세요?</h3>
        <WebtoonList webtoons={recommendations.randomWebtoons} />
      </div>
    </section>
  );
};

export default WebtoonRecommendationSection;
