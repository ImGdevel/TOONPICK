import React from 'react';
import { Link } from 'react-router-dom';
import { Webtoon } from '../../types/webtoon';
import WebtoonItem from '../WebtoonItem';
import styles from './WebtoonGrid.module.css';

interface WebtoonGridProps {
  webtoons: Webtoon[];
}

const WebtoonGrid: React.FC<WebtoonGridProps> = ({ webtoons }) => {
  return (
    <div className={styles.grid}>
      {webtoons.map((webtoon) => (
        <Link to={`/webtoon/${webtoon.id}`} key={webtoon.id} className={styles.gridItem}>
          <WebtoonItem webtoon={webtoon} />
        </Link>
      ))}
    </div>
  );
};

export default WebtoonGrid; 