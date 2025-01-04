import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Webtoon } from '../types/webtoon';
import { SearchPageProps } from '../types/page';
import WebtoonService from '@/services/webtoonService';
import styles from './SearchPage.module.css';

const SearchPage: React.FC<SearchPageProps> = ({ initialQuery }) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [query, setQuery] = useState<string>(initialQuery || '');
  const [results, setResults] = useState<Webtoon[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const searchQuery = searchParams.get('q');
    if (searchQuery) {
      setQuery(searchQuery);
      handleSearch(searchQuery);
    }
  }, [searchParams]);

  const handleSearch = async (searchQuery: string) => {
    try {
      setIsLoading(true);
      setError(null);
      const response = await WebtoonService.searchWebtoons(searchQuery);
      setResults(response.data);
    } catch (error) {
      setError('검색 중 오류가 발생했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setSearchParams({ q: query });
  };

  return (
    <div className={styles.searchPage}>
      <form onSubmit={handleSubmit} className={styles.searchForm}>
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="웹툰 검색..."
          className={styles.searchInput}
        />
        <button type="submit" className={styles.searchButton}>
          검색
        </button>
      </form>

      {isLoading && <div>검색중...</div>}
      {error && <div className={styles.error}>{error}</div>}
      
      <div className={styles.searchResults}>
        {results.map((webtoon) => (
          <div key={webtoon.id}>
            {/* WebtoonCard 컴포넌트 */}
          </div>
        ))}
      </div>
    </div>
  );
};

export default SearchPage; 