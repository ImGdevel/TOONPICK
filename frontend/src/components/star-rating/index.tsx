import React, { useState } from 'react';
import styles from './style.module.css';
import { FaStar, FaStarHalfAlt } from 'react-icons/fa';

interface StarRatingProps {
  rating: number;
  maxRating?: number;
  interactive?: boolean;
  onChange?: (rating: number) => void;
  textColor?: string;
  starSize?: number;
}

const StarRating: React.FC<StarRatingProps> = ({
  rating,
  maxRating = 5,
  interactive = false,
  onChange,
  textColor = 'black',
  starSize = 24
}) => {
  const [score, setScore] = useState(rating);
  const [scoreFixed, setScoreFixed] = useState(rating);

  const handleLeftHalfEnter = (idx: number) => interactive && setScore(idx + 0.5);
  const handleRightHalfEnter = (idx: number) => interactive && setScore(idx + 1);

  const handleStarClick = () => {
    if (interactive && onChange) {
      setScoreFixed(score);
      onChange(score);
    }
  };

  const handleStarLeave = () => {
    if (interactive) {
      setScore(scoreFixed);
    }
  };

  return (
    <div className={styles.rowBox}>
      {Array.from({ length: maxRating }, (_, idx) => (
        <div key={idx} className={styles.starDiv}>
          {score - Math.floor(score) === 0.5 && Math.floor(score) === idx ? (
            <FaStarHalfAlt size={starSize} color="gold" />
          ) : idx + 1 > score ? (
            <FaStar size={starSize} color="lightGray" />
          ) : (
            <FaStar size={starSize} color="gold" />
          )}
          <div
            className={`${styles.halfStarOverlay} ${styles.left}`}
            onMouseEnter={() => handleLeftHalfEnter(idx)}
            onMouseLeave={handleStarLeave}
            onClick={handleStarClick}
            style={{ pointerEvents: interactive ? 'auto' : 'none' }}
          />
          <div
            className={`${styles.halfStarOverlay} ${styles.right}`}
            onMouseEnter={() => handleRightHalfEnter(idx)}
            onMouseLeave={handleStarLeave}
            onClick={handleStarClick}
            style={{ pointerEvents: interactive ? 'auto' : 'none' }}
          />
        </div>
      ))}
      <span className={styles.ratingDisplay} style={{ color: textColor }}>
        {  score ? score.toFixed(1) : '0' }
      </span>
    </div>
  );
};

export default StarRating; 