import React from 'react';
import { Link } from 'react-router-dom';
import { Webtoon } from '../../types/webtoon';
import WebtoonItem from '../WebtoonItem';
import styles from './WebtoonGrid.module.css';

interface WebtoonGridProps {
  webtoons: Array<{
    id: number;
    title: string;
    thumbnailUrl: string;
    // ... 기타 필요한 속성들
  }>;
}

const WebtoonGrid: React.FC<WebtoonGridProps> = ({ webtoons = [] }) => {
  return (
    <div className={styles.grid}>
      {webtoons?.map((webtoon) => (
        <div key={webtoon.id} className={styles.gridItem}>
          <Link to={`/webtoon/${webtoon.id}`}>
            <img src={webtoon.thumbnailUrl} alt={webtoon.title} />
            <h3>{webtoon.title}</h3>
          </Link>
        </div>
      ))}
    </div>
  );
};

export default WebtoonGrid; 