import React, { useState, useEffect, useCallback } from 'react';
import { useInView } from 'react-intersection-observer';
import SearchAndFilter from './components/SearchAndFilter';
import WebtoonRatingList from './components/WebtoonRatingList';
import styles from './style.module.css';

interface Webtoon {
  id: number;
  title: string;
  author: string;
  thumbnail: string;
  genres: string[];
  platform: string;
}

interface WebtoonRatingFormProps {
  onComplete: () => void;
}

const WebtoonRatingPage: React.FC<WebtoonRatingFormProps> = ({ onComplete }) => {
  const [webtoons, setWebtoons] = useState<Webtoon[]>([]);
  const [filteredWebtoons, setFilteredWebtoons] = useState<Webtoon[]>([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedGenres, setSelectedGenres] = useState<string[]>([]);
  const [selectedPlatforms, setSelectedPlatforms] = useState<string[]>([]);

  const { ref, inView } = useInView({
    threshold: 0,
  });

  const fetchWebtoons = useCallback(async (pageNum: number) => {
    setLoading(true);
    try {
      // TODO: 실제 API 호출로 대체
      const mockWebtoons: Webtoon[] = Array.from({ length: 10 }, (_, i) => ({
        id: (pageNum - 1) * 10 + i + 1,
        title: `웹툰 제목 ${(pageNum - 1) * 10 + i + 1}`,
        author: `작가 ${(pageNum - 1) * 10 + i + 1}`,
        thumbnail: `https://via.placeholder.com/100?text=Webtoon+${(pageNum - 1) * 10 + i + 1}`,
        genres: ['로맨스', '판타지'],
        platform: '네이버',
      }));

      if (pageNum === 1) {
        setWebtoons(mockWebtoons);
      } else {
        setWebtoons(prev => [...prev, ...mockWebtoons]);
      }
      setHasMore(mockWebtoons.length === 10);
    } catch (error) {
      console.error('웹툰 목록을 불러오는데 실패했습니다:', error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchWebtoons(page);
  }, [page, fetchWebtoons]);

  useEffect(() => {
    if (inView && !loading && hasMore) {
      setPage(prev => prev + 1);
    }
  }, [inView, loading, hasMore]);

  useEffect(() => {
    const filtered = webtoons.filter(webtoon => {
      const matchesSearch = webtoon.title.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesGenres = selectedGenres.length === 0 || 
        webtoon.genres.some(genre => selectedGenres.includes(genre));
      const matchesPlatforms = selectedPlatforms.length === 0 || 
        selectedPlatforms.includes(webtoon.platform);
      return matchesSearch && matchesGenres && matchesPlatforms;
    });
    setFilteredWebtoons(filtered);
  }, [webtoons, searchQuery, selectedGenres, selectedPlatforms]);

  const handleSearch = (query: string) => {
    setSearchQuery(query);
    setPage(1);
  };

  const handleFilterChange = (filters: { genres: string[]; platforms: string[] }) => {
    setSelectedGenres(filters.genres);
    setSelectedPlatforms(filters.platforms);
    setPage(1);
  };

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <h1 className={styles.title}>웹툰 평가</h1>
        
        <div className={styles.fixedSection}>
          <SearchAndFilter
            onSearch={handleSearch}
            onFilterChange={handleFilterChange}
          />
        </div>

        <div className={styles.scrollableSection}>
          <WebtoonRatingList
            webtoons={filteredWebtoons}
            onRatingComplete={onComplete}
          />

          {loading && <div className={styles.loading}>로딩 중...</div>}
          {!loading && hasMore && <div ref={ref} className={styles.loadMoreTrigger} />}
        </div>
      </div>
    </div>
  );
};

export default WebtoonRatingPage; 