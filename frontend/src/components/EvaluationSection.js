import React, { useState, useEffect } from 'react';
import CommentList from './CommentList';
import EvaluationModal from './EvaluationModal';
import styles from './EvaluationSection.module.css';

const EvaluationSection = ({ webtoonId, averageRating, userRating, userComment }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [rating, setRating] = useState(userRating || 0);
  const [comment, setComment] = useState(userComment || '');
  const [comments, setComments] = useState([]);

  useEffect(() => {
    // 웹툰 ID를 기반으로 코멘트 및 평가 데이터를 가져오는 로직
    // setComments()를 통해 데이터를 설정
  }, [webtoonId]);

  const handleRatingChange = (newRating) => {
    setRating(newRating);
  };

  const handleCommentChange = (e) => {
    setComment(e.target.value);
  };

  const handleSubmit = () => {
    // 평가를 제출하는 로직
    console.log('User rating:', rating);
    console.log('User comment:', comment);
    setIsModalOpen(false);
  };

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div className={styles['evaluation-section']}>
      <div className={styles['evaluation-row']}>
        {/* 전체 종합 평가 */}
        <div className={styles['overall-rating']}>
          <h3>전체 종합 평가</h3>
          <div>평균 별점: ★{averageRating?.toFixed(1) || 'N/A'}</div>
        </div>

        {/* 자신의 평가 */}
        <div className={styles['user-evaluation']}>
          <h3>나의 평가</h3>
          {userRating === 0 ? (
            <div className={styles['no-rating']}>
              <p>평가가 아직 없습니다.</p>
              <button onClick={openModal}>평가하기</button>
            </div>
          ) : (
            <div className={styles['user-rating-display']}>
              <p>나의 별점: ★{rating}</p>
              <p>코멘트: {comment || '코멘트가 없습니다.'}</p>
              <button onClick={openModal}>수정하기</button>
            </div>
          )}
        </div>
      </div>

      {/* 다른 사람들의 코멘트 */}
      <div className={styles['comment-section']}>
        <h3>다른 사람들의 평가</h3>
        <CommentList comments={comments} />
      </div>

      {/* 평가 모달 */}
      <EvaluationModal
        isOpen={isModalOpen}
        onClose={closeModal}
        rating={rating}
        onRatingChange={handleRatingChange}
        comment={comment}
        onCommentChange={handleCommentChange}
        onSubmit={handleSubmit}
      />
    </div>
  );
};

export default EvaluationSection;
