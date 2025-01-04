import React, { useState, useEffect, useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';
import { WebtoonsPageState } from '@/types/page';
import WebtoonService from '@/services/webtoonService';
import WebtoonGrid from '@/components/WebtoonGrid';
import Pagination from '@/components/Pagination';
import styles from './OngoingWebtoonsPage.module.css';

const OngoingWebtoonsPage: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [state, setState] = useState<WebtoonsPageState>({
    webtoons: [],
    currentPage: 1,
    totalPages: 1,
    isLoading: true,
    error: null
  });

  const fetchOngoingWebtoons = async (page: number) => {
    try {
      const response = await WebtoonService.getWebtoons(page);  
      setState(prev => ({
        webtoons: [...prev.webtoons, ...response.data],
        currentPage: page,
        totalPages: Math.ceil((response.total || 0) / 20),
        isLoading: false,
        error: null
      }));
    } catch (error) {
      setState(prev => ({
        ...prev,
        isLoading: false,
        error: '연재 중인 웹툰을 불러오는데 실패했습니다.'
      }));
    }
  };

  useEffect(() => {
    const page = Number(searchParams.get('page')) || 1;
    fetchOngoingWebtoons(page);
  }, [searchParams]);

  const handleScroll = useCallback(() => {
    if (window.innerHeight + document.documentElement.scrollTop !== document.documentElement.offsetHeight || state.isLoading) return;
    const nextPage = state.currentPage + 1;
    if (nextPage <= state.totalPages) {
      setSearchParams({ page: nextPage.toString() });
      fetchOngoingWebtoons(nextPage);
    }
  }, [state]);

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