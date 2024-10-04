import React, { useState } from 'react';
import styles from './StarRating.module.css';
import { FaStar, FaStarHalfAlt } from 'react-icons/fa';

function StarRating({ rating, interactive = false, onRatingChange = null, textColor = 'black', starSize = 24 }) {
  const [score, setScore] = useState(rating);
  const [scoreFixed, setScoreFixed] = useState(rating);

  const handleLeftHalfEnter = (idx) => interactive && setScore(idx + 0.5);
  const handleRightHalfEnter = (idx) => interactive && setScore(idx + 1);

  const handleStarClick = () => {
    if (interactive && onRatingChange) {
      setScoreFixed(score);
      onRatingChange(score);
    }
  };

  const handleStarLeave = () => {
    if (interactive) {
      setScore(scoreFixed);
    }
  };

  return (
    <div className={styles.rowBox}>
      {Array(5)
        .fill(0)
        .map((_, idx) => (
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
              style={{ pointerEvents: interactive ? 'auto' : 'none' }} // Disable events if not interactive
            />
            <div
              className={`${styles.halfStarOverlay} ${styles.right}`}
              onMouseEnter={() => handleRightHalfEnter(idx)}
              onMouseLeave={handleStarLeave}
              onClick={handleStarClick}
              style={{ pointerEvents: interactive ? 'auto' : 'none' }} // Disable events if not interactive
            />
          </div>
        ))}
      <span className={styles.ratingDisplay} style={{ color: textColor }}>
        {score.toFixed(1)}
      </span>
    </div>
  );
}

export default StarRating;
