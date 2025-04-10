import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './style.module.css';

import { Webtoon } from '@models/webtoon';
import WebtoonRatingCard from '@components/webtoon/webtoon-rating-card';

interface WebtoonRatingListSectionProps {
  webtoons: Webtoon[];
}

const WebtoonRatingListSection: React.FC<WebtoonRatingListSectionProps> = ({ webtoons }) => {
  const navigate = useNavigate();

  const handleWebtoonClick = (webtoonId: number) => {
    navigate(`/webtoon/${webtoonId}`);
  };

  return (
    <section className={styles.ratingListSection}>
      <div className={styles.webtoonGrid}>
        {webtoons.length > 0 ? (
          webtoons.map((webtoon) => (
            <WebtoonRatingCard 
              key={webtoon.id} 
              webtoon={webtoon} 
              onClick={() => handleWebtoonClick(webtoon.id)} 
            />
          ))
        ) : (
          <div className={styles.noWebtoons}>
            <p>평가할 웹툰이 없습니다.</p>
          </div>
        )}
      </div>
    </section>
  );
};

export default WebtoonRatingListSection;
