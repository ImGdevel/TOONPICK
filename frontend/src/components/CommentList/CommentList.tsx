import React, { useState, useEffect } from 'react';
import { FaThumbsUp } from 'react-icons/fa';
import StarRating from '@/components/StarRating';
import ReportModal from '@/components/ReportModal';
import WebtoonReviewService from '@/services/WebtoonReviewService';
import styles from './CommentList.module.css';

interface Comment {
  id: number;
  userId: {
    username: string;
    nickname: string;
    role: string;
    profilePicture: string;
  };
  rating: number;
  comment: string;
  likes: number;
  createdDate: string;
  modifiedDate?: string;
}

interface CommentStatus {
  likes: number;
  liked: boolean;
}

interface CommentListProps {
  webtoonId: number;
  comments: Comment[];
}

const CommentList: React.FC<CommentListProps> = ({ webtoonId, comments }) => {
  const [commentStatus, setCommentStatus] = useState<CommentStatus[]>([]);
  const [isReportModalOpen, setIsReportModalOpen] = useState<boolean>(false);
  const [reportCommentId, setReportCommentId] = useState<number | null>(null);
  const [reportReason, setReportReason] = useState<string>('');

  useEffect(() => {
    const initializeCommentStatus = async (): Promise<void> => {
      const likedResponse = await WebtoonReviewService.getLikedReviews(webtoonId);
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

  const handleLikeClick = async (index: number): Promise<void> => {
    const commentId = comments[index].id;
    const response = await WebtoonReviewService.toggleLikeForReview(webtoonId, commentId);
    
    if (response.success) {
      setCommentStatus((prevStatus) => {
        const updatedStatus = [...prevStatus];
        updatedStatus[index].liked = response.data;
        updatedStatus[index].likes += response.data ? 1 : -1;
        return updatedStatus;
      });
    }
  };

  const handleReportClick = (commentId: number): void => {
    setReportCommentId(commentId);
    setIsReportModalOpen(true);
  };

  const closeReportModal = (): void => {
    setIsReportModalOpen(false);
    setReportCommentId(null);
    setReportReason('');
  };

  const handleReportSubmit = async (): Promise<void> => {
    if (!reportReason.trim() || !reportCommentId) return;

    const reportData = { reason: reportReason };
    const response = await WebtoonReviewService.reportWebtoonReview(webtoonId, reportCommentId, reportData);
    
    if (response.success) {
      closeReportModal();
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
                <span className={styles['comment-date']}>
                  {new Date(comment.createdDate).toLocaleDateString()}
                </span>
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
          <div className={styles['comment-text']}>{comment.comment}</div>
        </div>
      ))}
      {isReportModalOpen && (
        <ReportModal
          isOpen={isReportModalOpen}
          onClose={closeReportModal}
          onSubmit={handleReportSubmit}
          webtoonId={webtoonId}
        />
      )}
    </div>
  );
};

export default CommentList; 