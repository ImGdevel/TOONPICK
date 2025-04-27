import React from 'react';
import { Review } from '@models/review';
import StarRating from '@components/star-rating';
import styles from './style.module.css';

interface ReviewListProps {
  reviews: Review[];
}

const ReviewList: React.FC<ReviewListProps> = ({ reviews }) => {
  if (!reviews.length) {
    return <div className={styles.noReviews}>작성한 리뷰가 없습니다.</div>;
  }

  return (
    <div className={styles.reviewList}>
      {reviews.map((review) => (
        <div key={review.id} className={styles.reviewItem}>
          <div className={styles.reviewHeader}>
            <StarRating rating={review.rating} interactive={false} />
            <span className={styles.date}>
              {new Date(review.createdAt).toLocaleDateString()}
            </span>
          </div>
          <p className={styles.content}>{review.comment}</p>
        </div>
      ))}
    </div>
  );
};

export default ReviewList; 