import React, { useState } from 'react';
import styles from './CommentList.module.css';
import ReportModal from './ReportModal';
import StarRating from './StarRating'; // Import StarRating

const CommentList = ({ comments }) => {
  // 좋아요 상태를 각 댓글별로 관리
  const [likeCounts, setLikeCounts] = useState(
    comments.map((comment) => Number(comment.likes) || 0) // likes가 숫자임을 보장
  );
  const [likedStatus, setLikedStatus] = useState(
    comments.map(() => false) // 초기에는 모두 좋아요 안한 상태로 설정
  );
  const [isReportModalOpen, setIsReportModalOpen] = useState(false);
  const [reportCommentId, setReportCommentId] = useState(null);

  const handleLikeClick = (index) => {
    const updatedLikes = [...likeCounts];
    const updatedLikedStatus = [...likedStatus];

    if (likedStatus[index]) {
      // 이미 좋아요를 눌렀다면 취소
      updatedLikes[index] -= 1;
      updatedLikedStatus[index] = false;
    } else {
      // 좋아요를 아직 누르지 않았다면 추가
      updatedLikes[index] += 1;
      updatedLikedStatus[index] = true;
    }

    setLikeCounts(updatedLikes);
    setLikedStatus(updatedLikedStatus);
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
            {/* 섹션 1: 프로필, 작성자, 작성일자, 별점 */}
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
              {/* 별점 부분을 StarRating으로 대체 */}
              <StarRating rating={comment.rating} interactive={false} />
            </div>

            {/* 섹션 2: 좋아요 버튼, 신고 버튼 */}
            <div className={styles['comment-right']}>
              <button
                className={`${styles['like-button']} ${likedStatus[index] ? styles['liked'] : ''}`}
                onClick={() => handleLikeClick(index)}
              >
                👍 {likeCounts[index]}
              </button>
              <button
                className={styles['report-button']}
                onClick={() => handleReportClick(comment.id)}
              >
                신고
              </button>
            </div>
          </div>

          {/* 코멘트 내용 */}
          <div className={styles['comment-text']}>{comment.text}</div>
        </div>
      ))}

      {/* 신고 모달 */}
      {isReportModalOpen && (
        <ReportModal commentId={reportCommentId} onClose={closeReportModal} />
      )}
    </div>
  );
};

export default CommentList;
