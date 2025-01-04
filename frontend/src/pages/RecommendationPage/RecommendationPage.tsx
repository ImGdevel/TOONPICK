import React, { useState, useEffect } from 'react';
import { Webtoon } from '@/types/webtoon';
import WebtoonService from '@/services/webtoonService';
import WebtoonGrid from '@/components/WebtoonGrid';
import styles from './RecommendationPage.module.css';

interface RecommendationPageState {
  personalizedWebtoons: Webtoon[];
  popularWebtoons: Webtoon[];
  similarWebtoons: Webtoon[];
  isLoading: boolean;
  error: string | null;
}

const RecommendationPage: React.FC = () => {
  const [state, setState] = useState<RecommendationPageState>({
    personalizedWebtoons: [],
    popularWebtoons: [],
    similarWebtoons: [],
    isLoading: true,
    error: null
  });

  useEffect(() => {
    const fetchRecommendations = async () => {
      try {
        const [personalized, popular, similar] = await Promise.all([
          WebtoonService.getRecommendedWebtoons(),
          WebtoonService.getPopularWebtoons(),
          WebtoonService.getRecentWebtoons()
        ]);

        setState({
          personalizedWebtoons: personalized.data,
          popularWebtoons: popular.data,
          similarWebtoons: similar.data,
          isLoading: false,
          error: null
        });
      } catch (error) {
        setState(prev => ({
          ...prev,
          isLoading: false,
          error: '추천 웹툰을 불러오는데 실패했습니다.'
        }));
      }
    };

    fetchRecommendations();
  }, []);

  if (state.isLoading) return <div>로딩중...</div>;
  if (state.error) return <div>{state.error}</div>;

  return (
    <div className={styles.recommendationPage}>
      <h1>웹툰 추천</h1>

      <section className={styles.section}>
        <h2>맞춤 추천</h2>
        <p>회원님의 취향에 맞는 웹툰을 추천해드립니다.</p>
        <WebtoonGrid webtoons={state.personalizedWebtoons} />
      </section>

      <section className={styles.section}>
        <h2>인기 웹툰</h2>
        <p>지금 가장 인기있는 웹툰입니다.</p>
        <WebtoonGrid webtoons={state.popularWebtoons} />
      </section>

      <section className={styles.section}>
        <h2>비슷한 웹툰</h2>
        <p>최근에 보신 웹툰과 비슷한 작품들입니다.</p>
        <WebtoonGrid webtoons={state.similarWebtoons} />
      </section>
    </div>
  );
};

export default RecommendationPage; 