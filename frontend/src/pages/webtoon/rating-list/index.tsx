import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './style.module.css';

import WebtoonService from '@services/webtoon-service';
import { Webtoon } from '@models/webtoon';
import { SerializationStatus } from '@models/enum';
import WebtoonRatingListSection from './sections/webtoon-rating-list-section';
import WebtoonRatingFilterSection from './sections/webtoon-rating-filter-section';

const WebtoonRatingListPage: React.FC = () => {
  const [webtoons, setWebtoons] = useState<Webtoon[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [filters, setFilters] = useState({
    status: 'all', // 'all', 'ongoing', 'completed'
    genre: 'all',
    sortBy: 'rating', // 'rating', 'popularity', 'newest'
  });

  useEffect(() => {
    const fetchWebtoons = async () => {
      try {
        setLoading(true);
        const response = await WebtoonService.getWebtoons({
          page: 0,
          size: 20,
          sortBy: filters.sortBy,
          sortDir: 'desc',
          serializationStatus: filters.status === 'all' ? undefined : 
                             filters.status === 'ongoing' ? SerializationStatus.ONGOING : SerializationStatus.COMPLETED,
          genres: filters.genre === 'all' ? undefined : [filters.genre]
        });
        
        if (response?.success && response.data) {
          setWebtoons(response.data);
        } else {
          setError('웹툰 정보를 불러오는 데 실패했습니다.');
        }
      } catch (error) {
        console.error('Error fetching webtoons:', error);
        setError('웹툰 정보를 불러오는 데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchWebtoons();
  }, [filters]);

  const handleFilterChange = (newFilters: typeof filters) => {
    setFilters(newFilters);
  };

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className={styles.ratingListPage}>
      <h1 className={styles.pageTitle}>웹툰 평가</h1>
      
      <WebtoonRatingFilterSection 
        filters={filters} 
        onFilterChange={handleFilterChange} 
      />
      
      <WebtoonRatingListSection webtoons={webtoons} />
    </div>
  );
};

export default WebtoonRatingListPage;
