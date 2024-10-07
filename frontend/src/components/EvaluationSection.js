// src/components/EvaluationSection.js
import React, { useState, useEffect } from 'react';
import CommentList from './CommentList';
import EvaluationModal from './EvaluationModal';
import styles from './EvaluationSection.module.css';
import { getReviewsByWebtoon, createWebtoonReview } from '../services/webtoonReviewService';

const EvaluationSection = ({ webtoonId, averageRating, userRating, userComment }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [rating, setRating] = useState(userRating || 0);
  const [comment, setComment] = useState(userComment || '');
  const [comments, setComments] = useState([]);

  useEffect(() => {
    const fetchReviews = async () => {
      const response = await getReviewsByWebtoon(webtoonId);
      console.log('getReviewsByWebtoon response:', response, response.data); // 디버깅용 로그
      if (response.success) {
        if (response.data && Array.isArray(response.data.content)) {
          // response.data.content가 리뷰 배열인지 확인 후 설정
          setComments(response.data.content);
        } else {
          setComments([]); // 예상치 못한 구조일 경우 빈 배열로 설정
          console.warn('Unexpected response structure:', response.data);
        }
      } else {
        console.error('리뷰 가져오기 오류:', response.error);
        setComments([]); // 오류 발생 시 빈 배열로 설정
      }
    };
    fetchReviews();
  }, [webtoonId]);



  // 별점 변경 처리
  const handleRatingChange = (newRating) => {
    setRating(newRating);
  };

  // 코멘트 변경 처리
  const handleCommentChange = (e) => {
    setComment(e.target.value);
  };

  // 제출 처리: 서버에 평가 및 코멘트 전송
  const handleSubmit = async () => {
    const response = await createWebtoonReview(webtoonId, rating, comment);
    if (response.success) {
      setComments((prevComments) => [response.data, ...prevComments]); // 새로운 리뷰를 추가
      setIsModalOpen(false); // 모달 닫기
    } else {
      console.error('리뷰 생성 실패:', response.error);
      // 사용자에게 오류 알림 (예: 토스트 메시지)
    }
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
