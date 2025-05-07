import React from 'react';
import { Webtoon } from '@models/webtoon';
import WebtoonCard from '@components/webtoon-card';
import styles from './style.module.css';

interface WebtoonGridProps {
  webtoons: Webtoon[];
  lastWebtoonRef?: React.RefObject<HTMLDivElement>;
  rowLimit?: number;
}

const WebtoonGrid: React.FC<WebtoonGridProps> = ({ webtoons, lastWebtoonRef, rowLimit }) => {
  const itemsPerRow = 5;
  const maxItems = rowLimit ? rowLimit * itemsPerRow : webtoons.length;
  const displayWebtoons = webtoons.slice(0, maxItems);

  return (
    <div className={styles.grid}>
      {displayWebtoons.map((webtoon, index) => (
        <div
          className={styles.gridItem}
          key={webtoon.id || index}
          ref={index === displayWebtoons.length - 1 ? lastWebtoonRef : null} 
        >
          <WebtoonCard webtoon={webtoon} />
        </div>
      ))}
    </div>
  );
};

export default WebtoonGrid;
