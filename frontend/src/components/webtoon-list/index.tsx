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
  const [currentIndex, setCurrentIndex] = useState(0);
  const itemsToShow = 5; // 한 번에 보여줄 웹툰 수

  const handleNext = () => {
    if (currentIndex < webtoons.length - itemsToShow) {
      setCurrentIndex(currentIndex + 1);
    }
  };

  const handlePrev = () => {
    if (currentIndex > 0) {
      setCurrentIndex(currentIndex - 1);
    }
  };

  return (
    <div className={styles.webtoonList}>
      <div className={styles.carousel} style={{ transform: `translateX(-${currentIndex * (size + 10)}px)` }}>
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
      <div className={styles.navigation}>
        <button onClick={handlePrev} className={`${styles.navButton} ${currentIndex === 0 ? styles.disabled : ''}`}>
          ◀
        </button>
        <button onClick={handleNext} className={`${styles.navButton} ${currentIndex >= webtoons.length - itemsToShow ? styles.disabled : ''}`}>
          ▶
        </button>
      </div>
    </div>
  );
};

export default WebtoonList;