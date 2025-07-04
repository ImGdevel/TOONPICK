import React, { useState, useEffect, useRef } from 'react';
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
  const [displayedWebtoons, setDisplayedWebtoons] = useState<Webtoon[]>([]);
  const [ratings, setRatings] = useState<Record<number, { status: 'not_viewed' | 'viewing' | 'viewed'; rating?: number }>>({});
  const [currentIndex, setCurrentIndex] = useState(0);
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    // 처음에 10개의 웹툰을 표시
    setDisplayedWebtoons(webtoons.slice(0, 10));
  }, [webtoons]);

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

  const loadMoreWebtoons = () => {
    const nextIndex = currentIndex + 10;
    if (nextIndex < webtoons.length) {
      setDisplayedWebtoons(prev => [...prev, ...webtoons.slice(currentIndex, nextIndex)]);
      setCurrentIndex(nextIndex);
    } else {
      onRatingComplete();
    }
  };

  useEffect(() => {
    const container = containerRef.current;
    if (!container) return;

    const handleScroll = () => {
      const { scrollTop, scrollHeight, clientHeight } = container;
      if (scrollHeight - scrollTop <= clientHeight + 100) {
        loadMoreWebtoons();
      }
    };

    container.addEventListener('scroll', handleScroll);
    return () => {
      container.removeEventListener('scroll', handleScroll);
    };
  }, [currentIndex, webtoons]);

  return (
    <div ref={containerRef} className={styles.container}>
      {displayedWebtoons.map(webtoon => (
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
    </div>
  );
};

export default WebtoonRatingList; 