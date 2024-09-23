import React from 'react';
import styles from './StarRating.module.css';

const StarRating = ({ rating, onRatingChange, interactive = true, scale = 1 }) => {
  const handleStarClick = (starRating) => {
    if (interactive && onRatingChange) {
      onRatingChange(starRating);
    }
  };

  return (
    <div className={styles['star-rating']} style={{ fontSize: `${scale}em` }}>
      {[1, 2, 3, 4, 5].map((star) => (
        <span
          key={star}
          className={`${styles['star']} ${star <= rating ? styles['filled'] : ''}`}
          onClick={() => handleStarClick(star)}
          role="button"
          aria-hidden="true"
        >
          ★
        </span>
      ))}
      {interactive && <span className={styles['current-rating']}> {rating.toFixed(1)}</span>}
    </div>
  );
};

export default StarRating;
