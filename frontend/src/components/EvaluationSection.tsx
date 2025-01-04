import React, { useState } from 'react';
import StarRating from './StarRating';
import EvaluationModal from './EvaluationModal';
import styles from './EvaluationSection.module.css';

interface EvaluationSectionProps {
  webtoonId: number;
  currentRating?: number;
  totalRatings: number;
  averageRating: number;
  onRatingSubmit: (rating: number, comment: string) => Promise<void>;
}

const EvaluationSection: React.FC<EvaluationSectionProps> = ({
  webtoonId,
  currentRating,
  totalRatings,
  averageRating,
  onRatingSubmit
}) => {
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [selectedRating, setSelectedRating] = useState<number>(0);

  const handleRatingClick = (rating: number): void => {
    setSelectedRating(rating);
    setIsModalOpen(true);
  };

  return (
    <div className={styles.evaluationSection}>
      <div className={styles.ratingOverview}>
        <div className={styles.averageRating}>
          <h3>평균 평점</h3>
          <StarRating rating={averageRating} interactive={false} size="large" />
          <p>{averageRating.toFixed(1)} / 5.0 ({totalRatings}명)</p>
        </div>
        <div className={styles.userRating}>
          <h3>내 평가</h3>
          <StarRating
            rating={currentRating || 0}
            interactive={true}
            onChange={handleRatingClick}
            size="large"
          />
        </div>
      </div>

      {isModalOpen && (
        <EvaluationModal
          initialRating={selectedRating}
          onSubmit={onRatingSubmit}
          onClose={() => setIsModalOpen(false)}
        />
      )}
    </div>
  );
};

export default EvaluationSection; 