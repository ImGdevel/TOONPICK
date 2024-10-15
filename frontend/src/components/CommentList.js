// CommentList.jsx
import React, { useState, useEffect, Suspense } from 'react';
import PropTypes from 'prop-types';
import styles from './CommentList.module.css';
import ReportModal from './ReportModal';
import StarRating from './StarRating';
import { FaThumbsUp } from 'react-icons/fa';
import { toggleLikeForReview, reportWebtoonReview, getLikedReviews } from '../services/webtoonReviewService';

const CommentList = ({ webtoonId, comments }) => {
  const [commentStatus, setCommentStatus] = useState([]);
  const [isReportModalOpen, setIsReportModalOpen] = useState(false);
  const [reportCommentId, setReportCommentId] = useState(null);
  const [reportReason, setReportReason] = useState('');

  useEffect(() => {
    const initializeCommentStatus = async () => {
      const likedResponse = await getLikedReviews(webtoonId);
      if (likedResponse.success) {
        const likedReviewIds = likedResponse.data;
        setCommentStatus(
          comments.map((comment) => ({
            likes: typeof comment.likes === 'number' && !isNaN(comment.likes) ? comment.likes : 0,
            liked: likedReviewIds.includes(comment.id),
          }))
        );
      } else {
        setCommentStatus(
          comments.map((comment) => ({
            likes: typeof comment.likes === 'number' && !isNaN(comment.likes) ? comment.likes : 0,
            liked: false,
          }))
        );
      }
    };

    initializeCommentStatus();
  }, [webtoonId, comments]);

  const handleLikeClick = async (index) => {
    const commentId = comments[index].id;

    const response = await toggleLikeForReview(webtoonId, commentId);
    if (response.success) {
      console.log(response.data)
      setCommentStatus((prevStatus) => {
        const updatedStatus = [...prevStatus];
        updatedStatus[index].liked = response.data;
        updatedStatus[index].likes += response.data ? 1 : -1;
        return updatedStatus;
      });
    } else {
      console.error('Failed to toggle like:', response.error);
      // 필요 시, 사용자에게 오류 메시지 표시
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
      return;
    }

    const reportData = { reason: reportReason };
    const response = await reportWebtoonReview(reportCommentId, reportData);
    if (response.success) {
      closeReportModal();
    } else {
      console.error('Failed to report review:', response.error);
      // 필요 시, 사용자에게 오류 메시지 표시
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
                src={comment.userId.profilePicture}
                alt={`${comment.userId.nickname} profile`}
                className={styles['comment-profile']}
              />
              <div className={styles['comment-info']}>
                <span className={styles['comment-author']}>{comment.userId.nickname}</span>
                <span className={styles['comment-date']}>{new Date(comment.createdDate).toLocaleDateString()}</span>
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
              <button className={styles['report-button']} onClick={() => handleReportClick(comment.id)}>
                신고
              </button>
            </div>
          </div>
          <div className={styles['comment-text']}>{comment.comment}</div>
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
  webtoonId: PropTypes.number.isRequired,
  comments: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      webtoonId: PropTypes.number.isRequired,
      userId: PropTypes.shape({
        username: PropTypes.string.isRequired,
        nickname: PropTypes.string.isRequired,
        role: PropTypes.string.isRequired,
        profilePicture: PropTypes.string.isRequired,
      }).isRequired,
      rating: PropTypes.number.isRequired,
      comment: PropTypes.string.isRequired,
      likes: PropTypes.number.isRequired,
      createdDate: PropTypes.string.isRequired,
      modifiedDate: PropTypes.string,
    })
  ).isRequired,
};

export default CommentList;
