// src/components/WebtoonList.js
import React, { useState } from 'react';
import WebtoonItem from './WebtoonItem';
import styles from './WebtoonList.module.css';

const WebtoonList = ({ webtoons }) => {
  const [startIndex, setStartIndex] = useState(0);
  const itemsPerPage = 6; 

  const nextPage = () => {
    setStartIndex((prevIndex) => (prevIndex + itemsPerPage) % webtoons.length);
  };

  const prevPage = () => {
    setStartIndex((prevIndex) =>
      prevIndex === 0 ? webtoons.length - itemsPerPage : prevIndex - itemsPerPage
    );
  };

  const displayedWebtoons = webtoons.slice(startIndex, startIndex + itemsPerPage);

  return (
    <div className={styles['webtoon-list-container']}>
      <button className={`${styles['list-control']} ${styles.prev}`} onClick={prevPage}>
        &#10094;
      </button>
      <div className={styles['webtoon-list']}>
        {displayedWebtoons.map((webtoon) => (
          <WebtoonItem key={webtoon.id} webtoon={webtoon} />
        ))}
      </div>
      <button className={`${styles['list-control']} ${styles.next}`} onClick={nextPage}>
        &#10095;
      </button>
    </div>
  );
};

export default WebtoonList;
