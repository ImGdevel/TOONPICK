import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Webtoon, Platform, SerializationStatus } from '@models/webtoon';
import WebtoonService from '@services/webtoon-service';
import WebtoonRecommendationService from '@services/webtoon-recommendation-service';
import { useAuth } from '@hooks/use-auth';
import WebtoonCard from '@components/webtoon-card';
import FilterBar from '@components/filter-bar';
import Pagination from '@components/pagination';
import styles from './style.module.css';

interface Filters {
  platform: Platform | 'all';
  status: 'all' | 'ongoing' | 'completed';
  genre: string;
  sortBy: string;
}

const WebtoonRatingListPage: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [webtoons, setWebtoons] = useState<Webtoon[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [filters, setFilters] = useState<Filters>({
    platform: 'all',
    status: 'all',
    genre: 'all',
    sortBy: 'rating'
  });

  useEffect(() => {
    const fetchWebtoons = async () => {
      try {
        setLoading(true);
        
        let response;
        
        // 로그인한 사용자인 경우 추천 서비스 사용
        if (user) {
          response = await WebtoonRecommendationService.getPopularUnreadWebtoons(
            user.id,
            {
              page,
              size: 20,
              sortBy: filters.sortBy,
              sortDir: 'desc',
              platform: filters.platform === 'all' ? undefined : filters.platform,
              serializationStatus: filters.status === 'all' ? undefined :
                                 filters.status === 'ongoing' ? SerializationStatus.ONGOING : SerializationStatus.COMPLETED,
              genres: filters.genre === 'all' ? undefined : [filters.genre]
            }
          );
        } else {
          // 비로그인 사용자는 일반 서비스 사용
          response = await WebtoonService.getWebtoons({
            page,
            size: 20,
            sortBy: filters.sortBy,
            sortDir: 'desc',
            platform: filters.platform === 'all' ? undefined : filters.platform,
            serializationStatus: filters.status === 'all' ? undefined :
                               filters.status === 'ongoing' ? SerializationStatus.ONGOING : SerializationStatus.COMPLETED,
            genres: filters.genre === 'all' ? undefined : [filters.genre]
          });
        }

        setWebtoons(response.data);
        setTotalPages(Math.ceil(response.total / 20));
      } catch (error) {
        console.error('Error fetching webtoons:', error);
        setError('웹툰 정보를 불러오는 데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchWebtoons();
  }, [page, filters, user]);

  const handleFilterChange = (newFilters: Partial<Filters>) => {
    setFilters(prev => ({ ...prev, ...newFilters }));
    setPage(0); // 필터 변경 시 첫 페이지로 이동
  };

  const handlePageChange = (newPage: number) => {
    setPage(newPage);
  };

  const handleWebtoonClick = (webtoonId: number) => {
    navigate(`/webtoons/${webtoonId}`);
  };

  if (loading) return <div className={styles.loading}>로딩 중...</div>;
  if (error) return <div className={styles.error}>{error}</div>;

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>웹툰 평점 목록</h1>
      
      <FilterBar
        filters={filters}
        onFilterChange={handleFilterChange}
      />
      
      <div className={styles.webtoonGrid}>
        {webtoons.map(webtoon => (
          <WebtoonCard
            key={webtoon.id}
            webtoon={webtoon}
            onClick={() => handleWebtoonClick(webtoon.id)}
          />
        ))}
      </div>
      
      <Pagination
        currentPage={page}
        totalPages={totalPages}
        onPageChange={handlePageChange}
      />
    </div>
  );
};

export default WebtoonRatingListPage;
