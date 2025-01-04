import React from 'react';
import { FaStar } from 'react-icons/fa';
import styles from './StarRating.module.css';

interface StarRatingProps {
  rating?: number;
  interactive?: boolean;
  onChange?: (rating: number) => void;
  size?: 'small' | 'medium' | 'large';
  textColor?: string;
}

const StarRating: React.FC<StarRatingProps> = ({ 
  rating = 0,
  interactive = false, 
  onChange,
  size = 'medium',
  textColor = 'inherit'
}) => {
  const stars = Array.from({ length: 5 }, (_, index) => index + 1);

  return (
    <div className={`${styles.starRating} ${styles[size]}`} style={{ color: textColor }}>
      {stars.map((star) => (
        <FaStar
          key={star}
          className={`${styles.star} ${star <= rating ? styles.filled : ''}`}
          onClick={() => interactive && onChange?.(star)}
        />
      ))}
    </div>
  );
};

export default StarRating; 