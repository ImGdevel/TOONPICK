import React, { useState, useEffect } from 'react';
import WebtoonItem from '../components/WebtoonItem';
import styles from './CompletedWebtoonsPage.module.css'; 
import { getCompletedWebtoons } from '../services/webtoonService';

const CompletedWebtoonsPage = () => {
  const [webtoons, setWebtoons] = useState([]);
  const [page, setPage] = useState(0); // 페이지는 0부터 시작
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);

  useEffect(() => {
    console.log(`Fetching page: ${page}`); // 페이지 번호 확인 로그
    const fetchWebtoons = async () => {
      setLoading(true);
  
      try {
        const response = await getCompletedWebtoons(page);
        if (response.success) {
          if (response.data.length === 0) {
            setHasMore(false);
          } else {
            // 중복 데이터 방지
            setWebtoons((prevWebtoons) => {
              const newWebtoons = response.data.filter(
                (newWebtoon) => !prevWebtoons.some((prevWebtoon) => prevWebtoon.id === newWebtoon.id)
              );
              return [...prevWebtoons, ...newWebtoons];
            });
          }
        } else {
          console.error('완결된 웹툰을 가져오는 데 실패했습니다.');
        }
      } catch (error) {
        console.error('완결된 웹툰을 가져오는 중 오류가 발생했습니다:', error);
      } finally {
        setLoading(false);
      }
    };
  
    fetchWebtoons();
  }, [page]);

  const handleScroll = () => {
    if (window.innerHeight + document.documentElement.scrollTop === document.documentElement.offsetHeight) {
      if (!loading && hasMore) {
        setPage((prevPage) => prevPage + 1); // 다음 페이지 로드
      }
    }
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [loading, hasMore]);

  return (
    <div className={styles.explore}>
      <h1>완결된 웹툰</h1>
      <div className={styles['webtoon-list']}>
        {webtoons.map((webtoon, index) => (
          <WebtoonItem key={webtoon.id} webtoon={webtoon} />
        ))}
      </div>
      {loading && <p>Loading...</p>}
      {!hasMore && <p>더 이상 웹툰이 없습니다</p>}
    </div>
  );
};

export default CompletedWebtoonsPage;
