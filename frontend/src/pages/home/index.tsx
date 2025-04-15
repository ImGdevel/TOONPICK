import React, { useState, useEffect } from 'react';
import styles from './style.module.css';
import { Webtoon } from '@models/webtoon';  
import WebtoonGrid from '@components/webtoon-grid';


export interface HomePageState {
  popularWebtoons: Webtoon[];
  recentWebtoons: Webtoon[];
  isLoading: boolean;
  error: string | null;
}

const HomePage: React.FC = () => {
  const [state, setState] = useState<HomePageState>({
    popularWebtoons: [],
    recentWebtoons: [],
    isLoading: true,
    error: null
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [popular, recent] = await Promise.all([
          [],
          []
        ]);

        setState({
          popularWebtoons: [],
          recentWebtoons: [],
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

      </section>

      {/* 최신 웹툰 섹션 */}
      <section className={styles.section}>
        <h2>최신 웹툰</h2>

      </section>
    </div>
  );
};

export default HomePage; 