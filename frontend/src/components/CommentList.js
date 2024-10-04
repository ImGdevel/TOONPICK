import React, { useState, useEffect } from 'react';
import styles from './CommentList.module.css';
import ReportModal from './ReportModal';
import StarRating from './StarRating'; 
import { FaThumbsUp } from 'react-icons/fa';

const CommentList = ({ comments }) => {
  const [likeCounts, setLikeCounts] = useState([]);
  const [likedStatus, setLikedStatus] = useState([]);
  const [isReportModalOpen, setIsReportModalOpen] = useState(false);
  const [reportCommentId, setReportCommentId] = useState(null);

  useEffect(() => {
    setLikeCounts(
      comments.map((comment) => (typeof comment.likes === 'number' && !isNaN(comment.likes) ? comment.likes : 0))
    );
    setLikedStatus(comments.map(() => false));
  }, [comments]);

  const handleLikeClick = (index) => {
    setLikeCounts((prevLikeCounts) => {
      const updatedLikes = [...prevLikeCounts];
      const currentLikes = updatedLikes[index] || 0;

      if (likedStatus[index]) {
        updatedLikes[index] = Math.max(0, currentLikes - 1);
      } else {
        updatedLikes[index] = currentLikes + 1;
      }
      return updatedLikes;
    });

    setLikedStatus((prevLikedStatus) => {
      const updatedLikedStatus = [...prevLikedStatus];
      updatedLikedStatus[index] = !updatedLikedStatus[index];
      return updatedLikedStatus;
    });
  };

  const handleReportClick = (commentId) => {
    setReportCommentId(commentId);
    setIsReportModalOpen(true);
  };

  const closeReportModal = () => {
    setIsReportModalOpen(false);
    setReportCommentId(null);
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
              <StarRating rating={comment.rating} interactive={false} textColor='white'/>
            </div>

            <div className={styles['comment-right']}>
              <button
                className={`${styles['like-button']} ${likedStatus[index] ? styles['liked'] : ''}`}
                onClick={() => handleLikeClick(index)}
              >
                <FaThumbsUp /> {likeCounts[index]}
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
        <ReportModal commentId={reportCommentId} onClose={closeReportModal} />
      )}
    </div>
  );
};

export default CommentList;
