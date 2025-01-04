import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Webtoon } from '../types/webtoon';
import { HomePageState } from '../types/page';
import { getPopularWebtoons, getRecentWebtoons, getRecommendedWebtoons } from '../services/webtoonService';
import styles from './HomePage.module.css';

const HomePage: React.FC = () => {
  const [state, setState] = useState<HomePageState>({
    popularWebtoons: [],
    recentWebtoons: [],
    recommendedWebtoons: [],
    isLoading: true,
    error: null
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [popular, recent, recommended] = await Promise.all([
          getPopularWebtoons(),
          getRecentWebtoons(),
          getRecommendedWebtoons()
        ]);

        setState({
          popularWebtoons: popular.data,
          recentWebtoons: recent.data,
          recommendedWebtoons: recommended.data,
          isLoading: false,
          error: null
        });
      } catch (error) {
        setState(prev => ({
          ...prev,
          isLoading: false,
          error: '데이터를 불러오는데 실패했습니다.'
        }));
      }
    };

    fetchData();
  }, []);

  if (state.isLoading) return <div>로딩중...</div>;
  if (state.error) return <div>{state.error}</div>;

  return (
    <div className={styles.homePage}>
      {/* 인기 웹툰 섹션 */}
      <section className={styles.section}>
        <h2>인기 웹툰</h2>
        <div className={styles.webtoonGrid}>
          {state.popularWebtoons.map((webtoon) => (
            <Link to={`/webtoon/${webtoon.id}`} key={webtoon.id}>
              {/* WebtoonCard 컴포넌트 */}
            </Link>
          ))}
        </div>
      </section>

      {/* 최신 웹툰 섹션 */}
      <section className={styles.section}>
        <h2>최신 웹툰</h2>
        <div className={styles.webtoonGrid}>
          {state.recentWebtoons.map((webtoon) => (
            <Link to={`/webtoon/${webtoon.id}`} key={webtoon.id}>
              {/* WebtoonCard 컴포넌트 */}
            </Link>
          ))}
        </div>
      </section>

      {/* 추천 웹툰 섹션 */}
      <section className={styles.section}>
        <h2>추천 웹툰</h2>
        <div className={styles.webtoonGrid}>
          {state.recommendedWebtoons.map((webtoon) => (
            <Link to={`/webtoon/${webtoon.id}`} key={webtoon.id}>
              {/* WebtoonCard 컴포넌트 */}
            </Link>
          ))}
        </div>
      </section>
    </div>
  );
};

export default HomePage; 