import React from 'react';
import { Review } from '@models/review';
import styles from './style.module.css';

interface ReviewSectionProps {
  reviews: Review[];
  topReviews: Review[];
}

const ReviewSection: React.FC<ReviewSectionProps> = ({ reviews, topReviews }) => {
  return (
    <section className={styles.reviewSection}>
      <h2 className={styles.sectionTitle}>내가 쓴 리뷰</h2>
      <div className={styles.reviewStats}>
        <div className={styles.statItem}>
          <span className={styles.statNumber}>{reviews.length}</span>
          <span className={styles.statLabel}>작성한 리뷰</span>
        </div>
        <div className={styles.statItem}>
          <span className={styles.statNumber}>
            {reviews.reduce((acc, review) => acc + review.likes, 0)}
          </span>
          <span className={styles.statLabel}>받은 공감</span>
        </div>
      </div>
      <div className={styles.topReviews}>
        <h3>공감 TOP3 리뷰</h3>
        {topReviews.map((review) => (
          <div key={review.id} className={styles.reviewCard}>
            <div className={styles.reviewHeader}>
              <span className={styles.webtoonTitle}>{review.webtoonId}</span>
              <span className={styles.reviewDate}>{review.createdAt}</span>
            </div>
            <p className={styles.reviewContent}>{review.comment}</p>
            <div className={styles.reviewFooter}>
              <span className={styles.likes}>{review.likes} 공감</span>
            </div>
          </div>
        ))}
      </div>
      <div className={styles.recentReviews}>
        <h3>최근 리뷰</h3>
        {reviews.slice(0, 5).map((review) => (
          <div key={review.id} className={styles.reviewCard}>
            <div className={styles.reviewHeader}>
              <span className={styles.webtoonTitle}>{review.webtoonId}</span>
              <span className={styles.reviewDate}>{review.createdAt}</span>
            </div>
            <p className={styles.reviewContent}>{review.comment}</p>
          </div>
        ))}
      </div>
    </section>
  );
};

export default ReviewSection;
