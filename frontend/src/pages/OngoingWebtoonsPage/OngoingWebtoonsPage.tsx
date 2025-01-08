import React, { useState, useEffect, useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Webtoon } from '@models/webtoon';
import WebtoonService from '@services/webtoonService';
import styles from './OngoingWebtoonsPage.module.css';
import WebtoonGrid from '@/components/WebtoonGrid';


// 상태 타입 정의
interface WebtoonsPageState {
  webtoons: Webtoon[];
  currentPage: number;
  totalPages: number;
  isLoading: boolean;
  error: string | null;
}

const OngoingWebtoonsPage: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [state, setState] = useState<WebtoonsPageState>({
    webtoons: [],
    currentPage: 1,
    totalPages: 1,
    isLoading: true,
    error: null,
  });

  const fetchOngoingWebtoons = async (page: number) => {
    try {
      const response = await WebtoonService.getWebtoons(page);
      if (response.success) {
        setState((prev) => ({
          ...prev,
          webtoons: [...prev.webtoons, ...(response.data || [])],
          currentPage: page,
          totalPages: Math.ceil((response.total || 0) / 20),
          isLoading: false,
          error: null,
        }));
      } else {
        setState((prev) => ({
          ...prev,
          isLoading: false,
          error: response.message || 'Failed to load webtoons.',
        }));
      }
    } catch (error) {
      setState((prev) => ({
        ...prev,
        isLoading: false,
        error: 'Failed to load ongoing webtoons.',
      }));
    }
  };

  useEffect(() => {
    const page = Number(searchParams.get('page')) || 1;
    setState((prev) => ({
      ...prev,
      webtoons: [], // 페이지 변경 시 기존 데이터를 초기화
      isLoading: true,
    }));
    fetchOngoingWebtoons(page);
  }, [searchParams]);

  const handleScroll = useCallback(() => {
    if (
      window.innerHeight + document.documentElement.scrollTop !==
        document.documentElement.offsetHeight ||
      state.isLoading
    )
      return;

    const nextPage = state.currentPage + 1;
    if (nextPage <= state.totalPages) {
      setSearchParams({ page: nextPage.toString() });
    }
  }, [state, setSearchParams]);

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [handleScroll]);

  if (state.error) return <div>{state.error}</div>;

  return (
    <div className={styles.ongoingWebtoonsPage}>
      <h1>연재 중인 웹툰</h1>
      {state.isLoading ? (
        <p>로딩 중...</p>
      ) : (
        <WebtoonGrid webtoons={state.webtoons} />
      )}
    </div>
  );
};

export default OngoingWebtoonsPage;
