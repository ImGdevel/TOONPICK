import React, { useState } from 'react';
import { Webtoon } from '@models/webtoon';
import WebtoonCard from '@components/webtoon-card';
import styles from './style.module.css';

interface WebtoonListProps {
  webtoons: Webtoon[];
  size?: number;
  showTags?: boolean;
}

const WebtoonList: React.FC<WebtoonListProps> = ({ webtoons, size = 220, showTags = true }) => {
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 5;
  const totalPages = Math.ceil(webtoons.length / itemsPerPage);

  const handleNext = () => {
    if (currentPage < totalPages - 1) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePrev = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };

  return (
    <div className={styles.webtoonList}>
      <div
        className={styles.carousel}
        style={{ transform: `translateX(-${currentPage * (size + 10) * 5}px)`, minHeight: `${size}px` }}
      >
        {webtoons.length > 0 ? (
          webtoons.map((webtoon) => (
            <WebtoonCard
              key={webtoon.id}
              webtoon={webtoon}
              showTags={showTags}
              size={size}
            />
          ))
        ) : (
          <div className={styles.emptyMessage}>웹툰이 없습니다.</div>
        )}
      </div>
      {webtoons.length > itemsPerPage && (
        <div className={styles.navigation}>
          {currentPage > 0 && (
            <button onClick={handlePrev} className={styles.navButton}>
              ◀
            </button>
          )}
          {currentPage < totalPages - 1 && (
            <button onClick={handleNext} className={styles.navButton}>
              ▶
            </button>
          )}
        </div>
      )}
    </div>
  );
};

export default WebtoonList;