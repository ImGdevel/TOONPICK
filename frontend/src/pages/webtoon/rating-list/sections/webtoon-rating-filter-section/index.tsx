import React from 'react';
import styles from './style.module.css';

interface WebtoonRatingFilterSectionProps {
  filters: {
    status: string;
    genre: string;
    sortBy: string;
  };
  onFilterChange: (filters: {
    status: string;
    genre: string;
    sortBy: string;
  }) => void;
}

const WebtoonRatingFilterSection: React.FC<WebtoonRatingFilterSectionProps> = ({ 
  filters, 
  onFilterChange 
}) => {
  const handleStatusChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    onFilterChange({ ...filters, status: e.target.value });
  };

  const handleGenreChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    onFilterChange({ ...filters, genre: e.target.value });
  };

  const handleSortChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    onFilterChange({ ...filters, sortBy: e.target.value });
  };

  return (
    <section className={styles.filterSection}>
      <div className={styles.filterGroup}>
        <label htmlFor="status">연재 상태</label>
        <select 
          id="status" 
          value={filters.status} 
          onChange={handleStatusChange}
        >
          <option value="all">전체</option>
          <option value="ongoing">연재중</option>
          <option value="completed">완결</option>
        </select>
      </div>

      <div className={styles.filterGroup}>
        <label htmlFor="genre">장르</label>
        <select 
          id="genre" 
          value={filters.genre} 
          onChange={handleGenreChange}
        >
          <option value="all">전체</option>
          <option value="action">액션</option>
          <option value="romance">로맨스</option>
          <option value="comedy">코미디</option>
          <option value="drama">드라마</option>
          <option value="fantasy">판타지</option>
          <option value="scifi">SF</option>
          <option value="horror">호러</option>
        </select>
      </div>

      <div className={styles.filterGroup}>
        <label htmlFor="sortBy">정렬</label>
        <select 
          id="sortBy" 
          value={filters.sortBy} 
          onChange={handleSortChange}
        >
          <option value="rating">평점순</option>
          <option value="popularity">인기순</option>
          <option value="newest">최신순</option>
        </select>
      </div>
    </section>
  );
};

export default WebtoonRatingFilterSection;