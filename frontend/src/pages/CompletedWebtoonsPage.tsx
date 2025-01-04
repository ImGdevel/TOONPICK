import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { WebtoonsPageState } from '../types/page';
import WebtoonService from '../services/webtoonService';
import WebtoonGrid from '../components/WebtoonGrid/index';
import Pagination from '../components/Pagination/index';
import styles from './CompletedWebtoonsPage.module.css';

const CompletedWebtoonsPage: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [state, setState] = useState<WebtoonsPageState>({
    webtoons: [],
    currentPage: 1,
    totalPages: 1,
    isLoading: true,
    error: null
  });

  useEffect(() => {
    const fetchCompletedWebtoons = async () => {
      try {
        const page = Number(searchParams.get('page')) || 1;
        const response = await WebtoonService.getCompletedWebtoons(page);
        
        setState({
          webtoons: response.data,
          currentPage: page,
          totalPages: Math.ceil((response.total || 0) / 20),
          isLoading: false,
          error: null
        });
      } catch (error) {
        setState(prev => ({
          ...prev,
          isLoading: false,
          error: '완결 웹툰을 불러오는데 실패했습니다.'
        }));
      }
    };

    fetchCompletedWebtoons();
  }, [searchParams]);

  const handlePageChange = (page: number) => {
    setSearchParams({ page: page.toString() });
  };

  if (state.isLoading) return <div>로딩중...</div>;
  if (state.error) return <div>{state.error}</div>;

  return (
    <div className={styles.completedWebtoonsPage}>
      <h1>완결 웹툰</h1>
      <WebtoonGrid webtoons={state.webtoons} />
      <Pagination 
        currentPage={state.currentPage}
        totalPages={state.totalPages}
        onPageChange={handlePageChange}
      />
    </div>
  );
};

export default CompletedWebtoonsPage; 