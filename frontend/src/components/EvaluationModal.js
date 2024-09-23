import React from 'react';
import StarRating from './StarRating';
import styles from './EvaluationModal.module.css';

const EvaluationModal = ({ isOpen, onClose, rating, onRatingChange, comment, onCommentChange, onSubmit }) => {
  if (!isOpen) return null;

  return (
    <>
      <div className={styles['overlay']} onClick={onClose} />
      <div className={styles['modal']}>
        <h3>웹툰 평가하기</h3>
        <StarRating rating={rating} onRatingChange={onRatingChange} />
        <textarea
          value={comment}
          onChange={onCommentChange}
          placeholder="평가에 대한 코멘트를 남겨주세요."
        />
        <div className={styles['modal-buttons']}>
          <button onClick={onSubmit}>작성</button>
          <button onClick={onClose}>취소</button>
        </div>
      </div>
    </>
  );
};

export default EvaluationModal;
