import React, { useState, useEffect } from 'react';
import { FiStar } from 'react-icons/fi';
import styles from './style.module.css';

interface Webtoon {
  id: number;
  title: string;
  thumbnail: string;
  genres: string[];
  author: string;
}

interface WebtoonRatingFormProps {
  onComplete: () => void;
}

const WebtoonRatingForm: React.FC<WebtoonRatingFormProps> = ({ onComplete }) => {
  const [webtoons, setWebtoons] = useState<Webtoon[]>([]);
  const [currentWebtoon, setCurrentWebtoon] = useState<Webtoon | null>(null);
  const [viewStatus, setViewStatus] = useState<'not_viewed' | 'viewing' | 'viewed'>('not_viewed');
  const [rating, setRating] = useState<number>(0);

  useEffect(() => {
    // TODO: API에서 웹툰 목록 가져오기
    const mockWebtoons: Webtoon[] = [
      {
        id: 1,
        title: '웹툰 제목 1',
        thumbnail: 'https://via.placeholder.com/100',
        genres: ['로맨스', '판타지'],
        author: '작가 1',
      },
      {
        id: 2,
        title: '웹툰 제목 1',
        thumbnail: 'https://via.placeholder.com/100',
        genres: ['로맨스', '판타지'],
        author: '작가 1',
      },
      {
        id: 3,
        title: '웹툰 제목 1',
        thumbnail: 'https://via.placeholder.com/100',
        genres: ['로맨스', '판타지'],
        author: '작가 1',
      },
      // 더 많은 웹툰 데이터...
    ];
    setWebtoons(mockWebtoons);
    setCurrentWebtoon(mockWebtoons[0]);
  }, []);

  const handleStatusChange = (status: 'not_viewed' | 'viewing' | 'viewed') => {
    setViewStatus(status);
    if (status === 'not_viewed') {
      setRating(0);
    }
  };

  const handleRatingChange = (value: number) => {
    if (viewStatus !== 'not_viewed') {
      setRating(value);
    }
  };

  const handleNext = () => {
    if (currentWebtoon) {
      // TODO: 평가 데이터 저장
      const remainingWebtoons = webtoons.filter((w) => w.id !== currentWebtoon.id);
      if (remainingWebtoons.length > 0) {
        setCurrentWebtoon(remainingWebtoons[0]);
        setWebtoons(remainingWebtoons);
        setViewStatus('not_viewed');
        setRating(0);
      } else {
        onComplete();
      }
    }
  };

  if (!currentWebtoon) return null;

  return (
    <div className={styles.webtoonCard}>
      <div className={styles.thumbnailContainer}>
        <img src={currentWebtoon.thumbnail} alt={currentWebtoon.title} className={styles.thumbnail} />
      </div>
      
      <div className={styles.info}>
        <h3 className={styles.title}>{currentWebtoon.title}</h3>
        <p className={styles.author}>{currentWebtoon.author}</p>
        <div className={styles.genres}>
          {currentWebtoon.genres.map((genre) => (
            <span key={genre} className={styles.genre}>{genre}</span>
          ))}
        </div>
      </div>

      <div className={styles.ratingSection}>
        <div className={styles.statusButtons}>
          <button
            className={`${styles.statusButton} ${viewStatus === 'not_viewed' ? styles.selected : ''}`}
            onClick={() => handleStatusChange('not_viewed')}
          >
            안봤어요
          </button>
          <button
            className={`${styles.statusButton} ${viewStatus === 'viewing' ? styles.selected : ''}`}
            onClick={() => handleStatusChange('viewing')}
          >
            보는 중
          </button>
          <button
            className={`${styles.statusButton} ${viewStatus === 'viewed' ? styles.selected : ''}`}
            onClick={() => handleStatusChange('viewed')}
          >
            봤어요
          </button>
        </div>

        {(viewStatus === 'viewing' || viewStatus === 'viewed') && (
          <div className={styles.starRating}>
            {[1, 2, 3, 4, 5].map((value) => (
              <button
                key={value}
                className={`${styles.star} ${rating >= value ? styles.filled : ''}`}
                onClick={() => handleRatingChange(value)}
              >
                <FiStar />
              </button>
            ))}
          </div>
        )}
      </div>

      <button className={styles.nextButton} onClick={handleNext}>
        다음
      </button>
    </div>
  );
};

export default WebtoonRatingForm; 