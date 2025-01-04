// src/components/WebtoonItem.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './WebtoonItem.module.css';

const WebtoonItem = ({ webtoon }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/webtoon/${webtoon.id}`);
  };

  const authors = webtoon.authors ? Array.from(webtoon.authors).map(author => author.name).join(', ') : 'Unknown Author';
  const averageRating = webtoon.averageRating !== undefined ? webtoon.averageRating.toFixed(1) : 'N/A';
  const truncatedTitle = webtoon.title.length > 30 ? `${webtoon.title.substring(0, 30)}...` : webtoon.title;

  return (
    <div className={styles['webtoon-item']} onClick={handleClick}>
      <div className={styles['thumbnail-container']}>
        <img src={webtoon.thumbnailUrl} alt={webtoon.title} className={styles['thumbnail-image']} />
      </div>
      <div className={styles['webtoon-info']}>
        <span className={styles['webtoon-title']}>{truncatedTitle}</span>
        <div className={styles['webtoon-meta']}>
          <span className={styles['webtoon-author']}>{authors}</span>
          <span className={styles['webtoon-rating']}>⭐ {averageRating}</span>
        </div>
      </div>
    </div>
  );
};

export default WebtoonItem;
