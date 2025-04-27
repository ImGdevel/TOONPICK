import React from 'react';
import styles from './style.module.css';
import { Review } from '@models/review';
import StarRating from '@components/star-rating';
import { FaThumbsUp } from 'react-icons/fa';

interface WebtoonReviewCardProps {
  review: Review;
  onLike: (reviewId: number) => Promise<void>;
  onReport: (reviewId: number) => void;
}

const WebtoonReviewCard: React.FC<WebtoonReviewCardProps> = ({ review, onLike, onReport }) => {

  return (
    <div className={styles.reviewCard}>
      <div className={styles.reviewHeader}>
        <div className={styles.userInfo}>
          <img
            src={review.profilePicture}
            alt={`${review.userName} profile`}
            className={styles.profilePicture}
          />
          <span className={styles.nickname}>{review.userName}</span>
        </div>
        <StarRating rating={review.rating} interactive={false} textColor="black" />
        <div className={styles.actions}>
          <button className={styles.likeButton} onClick={() => onLike(review.id)}>
            <FaThumbsUp /> {review.likes}
          </button>
          <button className={styles.reportButton} onClick={() => onReport(review.id)}>
            신고
          </button>
        </div>
      </div>
      {review.comment && <p className={styles.comment}>{review.comment}</p>}
    </div>
  );
};

export default WebtoonReviewCard; 