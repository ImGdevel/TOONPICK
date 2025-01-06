import React from 'react';
import { Webtoon } from '@models/webtoon';
import WebtoonCard from '@components/WebtoonCard';
import styles from './WebtoonGrid.module.css';

interface WebtoonGridProps {
  webtoons: Webtoon[];
}

const WebtoonGrid: React.FC<WebtoonGridProps> = ({ webtoons }) => {
  return (
    <div className={styles.grid}>
      {webtoons.map(webtoon => (
        <div className={styles.gridItem} key={webtoon.id}>
          <WebtoonCard webtoon={webtoon} />
        </div>
      ))}
    </div>
  );
};

export default WebtoonGrid; 