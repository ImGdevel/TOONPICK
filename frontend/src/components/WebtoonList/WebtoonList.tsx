import React, { useState } from 'react';
import { Webtoon } from '@models/webtoon';
import WebtoonCard from '@components/WebtoonCard';
import styles from './WebtoonList.module.css';

interface WebtoonListProps {
  webtoons: Webtoon[];
  showTags?: boolean;
}

const WebtoonList: React.FC<WebtoonListProps> = ({ webtoons, showTags = true }) => {
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

  const startIndex = currentPage * itemsPerPage;
  const currentWebtoons = webtoons.slice(startIndex, startIndex + itemsPerPage);

  return (
    <div className={styles.webtoonList}>
      {currentWebtoons.length > 0 ? (
        <div className={styles.carousel}>
          {currentWebtoons.map((webtoon) => (
            <WebtoonCard
              key={webtoon.id}
              webtoon={webtoon}
              showTags={showTags}
            />
          ))}
        </div>
      ) : (
        <p className={styles.emptyMessage}>웹툰이 없습니다.</p>
      )}
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