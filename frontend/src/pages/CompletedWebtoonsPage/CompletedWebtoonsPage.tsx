import React, { useState, useEffect, useRef } from 'react';
import WebtoonService from '@services/webtoonService';
import WebtoonGrid from '@components/WebtoonGrid';
import styles from './CompletedWebtoonsPage.module.css';
import { Webtoon } from '@models/webtoon';

const CompletedWebtoonsPage: React.FC = () => {
  const [webtoons, setWebtoons] = useState<Webtoon[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [isLastPage, setIsLastPage] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const observer = useRef<IntersectionObserver | null>(null);
  const lastWebtoonRef = useRef<HTMLDivElement | null>(null);

  const fetchCompletedWebtoons = async (page: number) => {
    const size = 30;
    setIsLoading(true);
    try {
      const response = await WebtoonService.getCompletedWebtoons(page, size);
      const { data, last } = response;

      setWebtoons((prev) => [...prev, ...(data || [])]);
      setIsLastPage(last || false);
    } catch (error) {
      setError('완결 웹툰을 불러오는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  // 초기 데이터 로드
  useEffect(() => {
    fetchCompletedWebtoons(0);
  }, []);

  // IntersectionObserver 설정
  useEffect(() => {
    const observerCallback = (entries: IntersectionObserverEntry[]) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting && !isLoading && !isLastPage) {
          setCurrentPage((prev) => prev + 1);
        }
      });
    };

    if (!observer.current) {
      observer.current = new IntersectionObserver(observerCallback);
    }

    const currentRef = lastWebtoonRef.current;
    if (currentRef) {
      observer.current.observe(currentRef);
    }

    return () => {
      if (currentRef) {
        observer.current?.unobserve(currentRef);
      }
    };
  }, [isLoading, isLastPage]);

  // 새로운 페이지 로드
  useEffect(() => {
    if (currentPage > 0 && !isLastPage) {
      fetchCompletedWebtoons(currentPage);
    }
  }, [currentPage, isLastPage]);

  return (
    <div className={styles.completedWebtoonsPage}>
      <h1>완결 웹툰</h1>

      {error ? (
        <div className={styles.error}>
          <p>{error}</p>
          <button onClick={() => fetchCompletedWebtoons(currentPage)}>재시도</button>
        </div>
      ) : (
        <>
          <WebtoonGrid
            webtoons={webtoons}
            lastWebtoonRef={lastWebtoonRef}
          />
          {isLoading && <div className={styles.spinner}>로딩중...</div>}
          {isLastPage && <p className={styles.endMessage}>모든 웹툰을 불러왔습니다.</p>}
        </>
      )}
    </div>
  );
};

export default CompletedWebtoonsPage;
