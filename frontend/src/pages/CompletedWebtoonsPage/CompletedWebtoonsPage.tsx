import React, { useState, useEffect, useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Webtoon } from '@models/webtoon';
import WebtoonService from '@services/webtoonService';
import WebtoonGrid from '@components/WebtoonGrid';
import styles from './CompletedWebtoonsPage.module.css';

export interface WebtoonsPageState {
  webtoons: Webtoon[];
  currentPage: number;
  totalPages: number;
  isLoading: boolean;
  error: string | null;
}

const CompletedWebtoonsPage: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [state, setState] = useState<WebtoonsPageState>({
    webtoons: [],
    currentPage: 0,
    totalPages: 1,
    isLoading: true,
    error: null
  });

  const fetchCompletedWebtoons = async (page: number) => {
    const size = 20; // 페이지 크기 설정
    const sortBy = 'title'; // 정렬 기준
    const sortDir = 'asc'; // 정렬 방향
    const genres = []; // 필요에 따라 장르를 설정

    try {
      const response = await WebtoonService.getCompletedWebtoons(page, size, sortBy, sortDir);
      setState((prev) => ({
        webtoons: [...prev.webtoons, ...(response.data || [])],
        currentPage: page,
        totalPages: Math.ceil((response.total || 0) / size),
        isLoading: false,
        error: null
      }));
    } catch (error) {
      setState(prev => ({
        ...prev,
        isLoading: false,
        error: '완결 웹툰을 불러오는데 실패했습니다.'
      }));
    }
  };

  useEffect(() => {
    const page = Number(searchParams.get('page')) || 0;
    fetchCompletedWebtoons(page);
  }, [searchParams]);

  const handleScroll = useCallback(() => {
    if (
      window.innerHeight + document.documentElement.scrollTop !==
        document.documentElement.offsetHeight ||
      state.isLoading ||
      state.currentPage >= state.totalPages
    ) return;

    const nextPage = state.currentPage + 1;
    setSearchParams({ page: nextPage.toString() });
    fetchCompletedWebtoons(nextPage);
  }, [state, setSearchParams]);

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [handleScroll]);

  if (state.error) return <div>{state.error}</div>;

  return (
    <div className={styles.completedWebtoonsPage}>
      <h1>완결 웹툰</h1>
      {state.isLoading ? (
        <p>로딩중...</p>
      ) : (
        <WebtoonGrid webtoons={state.webtoons} />
      )}
    </div>
  );
};

export default CompletedWebtoonsPage; 