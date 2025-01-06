import React, { useState } from 'react';
import StarRating from '@components/StarRating';
import styles from './EvaluationModal.module.css';

interface EvaluationModalProps {
  initialRating: number;
  onSubmit: (rating: number, comment: string) => Promise<void>;
  onClose: () => void;
}

const EvaluationModal: React.FC<EvaluationModalProps> = ({
  initialRating,
  onSubmit,
  onClose
}) => {
  const [rating, setRating] = useState<number>(initialRating);
  const [comment, setComment] = useState<string>('');
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const handleSubmit = async (e: React.FormEvent): Promise<void> => {
    e.preventDefault();
    if (isSubmitting) return;

    try {
      setIsSubmitting(true);
      await onSubmit(rating, comment);
      onClose();
    } catch (error) {
      console.error('평가 제출 실패:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modal}>
        <h2>웹툰 평가하기</h2>
        <form onSubmit={handleSubmit}>
          <div className={styles.ratingSection}>
            <StarRating
              rating={rating}
              interactive={true}
              onChange={setRating}
              size="large"
            />
          </div>
          <textarea
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="이 웹툰에 대한 의견을 남겨주세요"
            className={styles.commentInput}
          />
          <div className={styles.buttons}>
            <button 
              type="submit" 
              disabled={isSubmitting}
              className={styles.submitButton}
            >
              {isSubmitting ? '제출 중...' : '평가 제출'}
            </button>
            <button 
              type="button" 
              onClick={onClose}
              className={styles.cancelButton}
            >
              취소
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EvaluationModal; 