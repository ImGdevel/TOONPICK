import React from 'react';
import { Webtoon } from '@/types/webtoon';
import WebtoonCard from '@/components/WebtoonCard';
import styles from './WebtoonGrid.module.css';

interface WebtoonGridProps {
  webtoons: Webtoon[];
}

const WebtoonGrid: React.FC<WebtoonGridProps> = ({ webtoons }) => {
  return (
    <div className={styles.webtoonGrid}>
      {webtoons.map(webtoon => (
        <WebtoonCard key={webtoon.id} webtoon={webtoon} />
      ))}
    </div>
  );
};

export default WebtoonGrid; 