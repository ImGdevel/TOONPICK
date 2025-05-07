import React, { useState, useEffect } from 'react';
import styles from './style.module.css';
import { Webtoon } from '@models/webtoon';  
import WebtoonGrid from '@components/webtoon-grid';
import Carousel from '@components/carousel';
import WebtoonService from '@services/webtoon-service';


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

  // 캐러셀 아이템 상태 분리
  const [carouselItems, setCarouselItems] = useState([
    {
      imageUrl: "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80",
      link: "/webtoon/solo-leveling",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1519125323398-675f0ddb6308?auto=format&fit=crop&w=800&q=80",
      link: "/webtoon/true-beauty",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=800&q=80",
      link: "/webtoon/my-id-is-gangnam-beauty",
    },
  ]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [popularRes, recentRes] = await Promise.all([
          WebtoonService.getPopularWebtoons(10),
          WebtoonService.getRecentWebtoons(10)
        ]);

        setState({
          popularWebtoons: popularRes.success && popularRes.data ? popularRes.data : [],
          recentWebtoons: recentRes.success && recentRes.data ? recentRes.data : [],
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
        <Carousel items={carouselItems} />
      </section>


      {/* 인기 웹툰 섹션 */}
      <section className={styles.section}>
        <h2>인기 웹툰</h2>
         <WebtoonGrid webtoons={state.popularWebtoons || []} rowLimit={1} /> 
      </section>

      {/* 최신 웹툰 섹션 */}
      <section className={styles.section}>
        <h2>최신 웹툰</h2>
         <WebtoonGrid webtoons={state.recentWebtoons || []} rowLimit={2} /> 
      </section>
    </div>
  );
};

export default HomePage; 