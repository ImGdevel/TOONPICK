import React, { useState, useEffect } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { WebtoonListPageProps, WebtoonListPageState } from '../types/page';
import { getWebtoonList } from '../services/webtoonService';
import styles from './WebtoonListPage.module.css';

const WebtoonListPage: React.FC<WebtoonListPageProps> = ({ category }) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [state, setState] = useState<WebtoonListPageState>({
    webtoons: [],
    totalPages: 0,
    currentPage: 1,
    isLoading: true,
    error: null
  });

  useEffect(() => {
    const fetchWebtoons = async () => {
      try {
        const page = Number(searchParams.get('page')) || 1;
        const response = await getWebtoonList(page, 20);

        setState({
          webtoons: response.data,
          totalPages: Math.ceil(response.total / 20),
          currentPage: page,
          isLoading: false,
          error: null
        });
      } catch (error) {
        setState(prev => ({
          ...prev,
          isLoading: false,
          error: '웹툰 목록을 불러오는데 실패했습니다.'
        }));
      }
    };

    fetchWebtoons();
  }, [searchParams, category]);

  const handlePageChange = (page: number) => {
    setSearchParams({ page: page.toString() });
  };

  if (state.isLoading) return <div>로딩중...</div>;
  if (state.error) return <div>{state.error}</div>;

  return (
    <div className={styles.webtoonListPage}>
      <h1>{category || '전체'} 웹툰</h1>
      <div className={styles.webtoonGrid}>
        {state.webtoons.map((webtoon) => (
          <div key={webtoon.id}>
            {/* WebtoonCard 컴포넌트 */}
          </div>
        ))}
      </div>
      {/* Pagination 컴포넌트 */}
    </div>
  );
};

export default WebtoonListPage; 