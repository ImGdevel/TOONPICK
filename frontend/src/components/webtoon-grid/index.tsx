import React from 'react';
import { Webtoon } from '@models/webtoon';
import WebtoonCard from '@components/webtoon-card';
import styles from './style.module.css';

interface WebtoonGridProps {
  webtoons: Webtoon[];
  lastWebtoonRef?: React.RefObject<HTMLDivElement>;
}

const WebtoonGrid: React.FC<WebtoonGridProps> = ({ webtoons, lastWebtoonRef }) => {
  return (
    <div className={styles.grid}>
      {webtoons.map((webtoon, index) => (
        <div
          className={styles.gridItem}
          key={webtoon.id || index}
          ref={index === webtoons.length - 1 ? lastWebtoonRef : null} 
        >
          <WebtoonCard webtoon={webtoon} />
        </div>
      ))}
    </div>
  );
};

export default WebtoonGrid;
