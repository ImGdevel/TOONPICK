import React from 'react';
import styles from './StarRating.module.css';

interface StarRatingProps {
  rating: number;
  maxRating?: number;
  size?: 'small' | 'medium' | 'large';
  interactive?: boolean;
  onChange?: (rating: number) => void;
  textColor?: string;
}

const StarRating: React.FC<StarRatingProps> = ({
  rating,
  maxRating = 5,
  size = 'medium',
  interactive = false,
  onChange
}) => {
  const renderStar = (index: number): JSX.Element => {
    const filled = index < rating;
    const starClass = `${styles.star} ${filled ? styles.filled : ''} ${styles[size]}`;
    
    return (
      <span
        key={index}
        className={starClass}
        onClick={() => interactive && onChange && onChange(index + 1)}
        style={{ cursor: interactive ? 'pointer' : 'default' }}
      >
        ★
      </span>
    );
  };

  return (
    <div className={styles.starRating}>
      {[...Array(maxRating)].map((_, index) => renderStar(index))}
      <span className={styles.ratingText}>
        {rating.toFixed(1)}
      </span>
    </div>
  );
};

export default StarRating; 