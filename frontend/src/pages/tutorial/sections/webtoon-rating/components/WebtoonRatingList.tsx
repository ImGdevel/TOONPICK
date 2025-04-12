import React, { useState } from 'react';
import styles from './WebtoonRatingList.module.css';
import StarRating from 'src/components/star-rating';

interface Webtoon {
  id: number;
  title: string;
  author: string;
  thumbnail: string;
  genres: string[];
  platform: string;
}

interface WebtoonRatingListProps {
  webtoons: Webtoon[];
  onRatingComplete: () => void;
}

const WebtoonRatingList: React.FC<WebtoonRatingListProps> = ({ webtoons, onRatingComplete }) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [ratings, setRatings] = useState<Record<number, { status: 'not_viewed' | 'viewing' | 'viewed'; rating?: number }>>({});

  const handleStatusChange = (webtoonId: number, status: 'not_viewed' | 'viewing' | 'viewed') => {
    setRatings(prev => ({
      ...prev,
      [webtoonId]: { ...prev[webtoonId], status }
    }));
  };

  const handleRatingChange = (webtoonId: number, rating: number) => {
    setRatings(prev => ({
      ...prev,
      [webtoonId]: { ...prev[webtoonId], rating }
    }));
  };

  const handleNext = () => {
    if (currentIndex < webtoons.length - 1) {
      setCurrentIndex(prev => prev + 1);
    } else {
      onRatingComplete();
    }
  };

  const currentWebtoons = webtoons.slice(currentIndex, currentIndex + 3);

  return (
    <div className={styles.container}>
      {currentWebtoons.map(webtoon => (
        <div key={webtoon.id} className={styles.webtoonCard}>
          <div className={styles.webtoonInfo}>
            <img src={webtoon.thumbnail} alt={webtoon.title} className={styles.thumbnail} />
            <div className={styles.details}>
              <h3 className={styles.title}>{webtoon.title}</h3>
              <p className={styles.author}>{webtoon.author}</p>
              <div className={styles.genres}>
                {webtoon.genres.map(genre => (
                  <span key={genre} className={styles.genreTag}>{genre}</span>
                ))}
              </div>
            </div>
          </div>
          <div className={styles.ratingSection}>
            <div className={styles.statusButtons}>
              <button
                className={`${styles.statusButton} ${ratings[webtoon.id]?.status === 'not_viewed' ? styles.selected : ''}`}
                onClick={() => handleStatusChange(webtoon.id, 'not_viewed')}
              >
                안 봄
              </button>
              <button
                className={`${styles.statusButton} ${ratings[webtoon.id]?.status === 'viewing' ? styles.selected : ''}`}
                onClick={() => handleStatusChange(webtoon.id, 'viewing')}
              >
                보는 중
              </button>
              <button
                className={`${styles.statusButton} ${ratings[webtoon.id]?.status === 'viewed' ? styles.selected : ''}`}
                onClick={() => handleStatusChange(webtoon.id, 'viewed')}
              >
                봄
              </button>
            </div>
            {(ratings[webtoon.id]?.status === 'viewing' || ratings[webtoon.id]?.status === 'viewed') && (
              <div className={styles.ratingInput}>
                <label>평점:</label>
                <StarRating
                  rating={ratings[webtoon.id]?.rating || 0}
                  onChange={(rating) => handleRatingChange(webtoon.id, rating)}
                  interactive={true}
                />
              </div>
            )}
          </div>
        </div>
      ))}
      <button onClick={handleNext} className={styles.nextButton}>
        {currentIndex < webtoons.length - 1 ? '다음' : '완료'}
      </button>
    </div>
  );
};

export default WebtoonRatingList; 