import React, { useState } from 'react';
import { Webtoon } from '@models/webtoon';
import WebtoonCard from '@components/WebtoonCard';
import styles from './WebtoonList.module.css';

interface WebtoonListProps {
  webtoons: Webtoon[];
  size?: number;
  showTags?: boolean;
}

const WebtoonList: React.FC<WebtoonListProps> = ({ webtoons, size = 220, showTags = true }) => {
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 4;
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
        style={{ transform: `translateX(-${currentPage * (size + 10) * 5 }px)` }}
      >
        {webtoons.map((webtoon) => (
          <WebtoonCard
            key={webtoon.id}
            webtoon={webtoon}
            showTags={showTags}
            size={size}
          />
        ))}
      </div>
      <div className={styles.navigation}>
        <button onClick={handlePrev} disabled={currentPage === 0} className={styles.navButton}>
          ◀
        </button>
        <button onClick={handleNext} disabled={currentPage === totalPages - 1} className={styles.navButton}>
          ▶
        </button>
      </div>
    </div>
  );
};

export default WebtoonList;