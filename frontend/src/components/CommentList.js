// src/components/CommentList.js
import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import styles from './CommentList.module.css';
import ReportModal from './ReportModal';
import StarRating from './StarRating';
import { FaThumbsUp } from 'react-icons/fa';
import { toggleLikeForReview, reportWebtoonReview } from '../services/webtoonReviewService'; // API 함수 임포트

const CommentList = ({ comments }) => {
  const [commentStatus, setCommentStatus] = useState([]);
  const [isReportModalOpen, setIsReportModalOpen] = useState(false);
  const [reportCommentId, setReportCommentId] = useState(null);
  const [reportReason, setReportReason] = useState('');

  useEffect(() => {
    setCommentStatus(
      comments.map((comment) => ({
        likes: typeof comment.likes === 'number' && !isNaN(comment.likes) ? comment.likes : 0,
        liked: false,
      }))
    );
  }, [comments]);

  const handleLikeClick = async (index) => {
    const commentId = comments[index].id;
    const currentLikedStatus = commentStatus[index].liked;

    // API 호출하여 좋아요 토글
    const response = await toggleLikeForReview(commentId);

    if (response.success) {
      setCommentStatus((prevStatus) => {
        const updatedStatus = [...prevStatus];
        updatedStatus[index].liked = !currentLikedStatus;
        updatedStatus[index].likes = response.data.likes; // 서버에서 반환된 최신 좋아요 수로 업데이트
        return updatedStatus;
      });
    } else {
      console.error('좋아요 토글 실패:', response.error);
      // 사용자에게 오류 알림 (예: 토스트 메시지)
    }
  };

  const handleReportClick = (commentId) => {
    setReportCommentId(commentId);
    setIsReportModalOpen(true);
  };

  const closeReportModal = () => {
    setIsReportModalOpen(false);
    setReportCommentId(null);
    setReportReason('');
  };

  const handleReportSubmit = async () => {
    if (!reportReason.trim()) {
      // 사용자에게 신고 이유를 입력하도록 알림
      return;
    }

    const reportData = {
      reason: reportReason,
    };

    const response = await reportWebtoonReview(reportCommentId, reportData);

    if (response.success) {
      // 사용자에게 신고가 접수되었음을 알림
      closeReportModal();
      // 필요 시, 신고된 댓글을 목록에서 제거하거나 표시 변경
    } else {
      console.error('리뷰 신고 실패:', response.error);
      // 사용자에게 오류 알림
    }
  };

  if (!comments || comments.length === 0) {
    return <div className={styles['no-comments']}>아직 코멘트가 없습니다.</div>;
  }

  return (
    <div className={styles['comment-list']}>
      {comments.map((comment, index) => (
        <div key={comment.id} className={styles['comment-item']}>
          <div className={styles['comment-header']}>
            <div className={styles['comment-left']}>
              <img
                src={comment.profileImage}
                alt={`${comment.author} profile`}
                className={styles['comment-profile']}
              />
              <div className={styles['comment-info']}>
                <span className={styles['comment-author']}>{comment.author}</span>
                <span className={styles['comment-date']}>{comment.date}</span>
              </div>
              <StarRating rating={comment.rating} interactive={false} textColor="white" />
            </div>

            <div className={styles['comment-right']}>
              <button
                className={`${styles['like-button']} ${commentStatus[index]?.liked ? styles['liked'] : ''}`}
                onClick={() => handleLikeClick(index)}
              >
                <FaThumbsUp /> {commentStatus[index]?.likes || 0}
              </button>
              <button
                className={styles['report-button']}
                onClick={() => handleReportClick(comment.id)}
              >
                신고
              </button>
            </div>
          </div>

          <div className={styles['comment-text']}>{comment.text}</div>
        </div>
      ))}

      {isReportModalOpen && (
        <ReportModal
          commentId={reportCommentId}
          reason={reportReason}
          setReason={setReportReason}
          onClose={closeReportModal}
          onSubmit={handleReportSubmit}
        />
      )}
    </div>
  );
};

CommentList.propTypes = {
  comments: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      author: PropTypes.string.isRequired,
      profileImage: PropTypes.string.isRequired,
      rating: PropTypes.number.isRequired,
      text: PropTypes.string.isRequired,
      likes: PropTypes.number.isRequired,
      date: PropTypes.string.isRequired,
    })
  ).isRequired,
};

export default CommentList;
