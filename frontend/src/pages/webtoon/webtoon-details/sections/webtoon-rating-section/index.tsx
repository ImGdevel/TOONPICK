import React, { useContext, useState } from 'react';
import { Webtoon } from '@models/webtoon';
import { Review } from '@models/review';
import { AuthContext } from '@contexts/auth-context';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import { FiMoreVertical, FiThumbsUp, FiFlag } from 'react-icons/fi';
import styles from './style.module.css';

interface WebtoonRatingSectionProps {
  webtoon: Webtoon;
}

const WebtoonRatingSection: React.FC<WebtoonRatingSectionProps> = ({ webtoon }) => {
  const { state } = useContext(AuthContext);
  const [userRating, setUserRating] = useState<number>(0);
  const [showCommentForm, setShowCommentForm] = useState<boolean>(false);
  const [userComment, setUserComment] = useState<string>('');
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);
  const [sortBy, setSortBy] = useState<'popular' | 'recent'>('popular');
  const [showMoreMenu, setShowMoreMenu] = useState<number | null>(null);

  // 더미 데이터
  const hasUserRated = state && userRating > 0;
  
  // 더미 평가 데이터
  const dummyReviews: Review[] = [
    {
      id: 1,
      webtoonId: 1,
      userId: '웹툰팬1',
      userName: '팬1',
      profilePicture: 'https://via.placeholder.com/40',
      rating: 5,
      comment: '정말 재미있는 웹툰입니다. 작화도 좋고 스토리도 흥미롭습니다.',
      createdAt: '2023-05-15T14:30:00Z',
      modifiedAt: '2023-05-15T14:30:00Z',
      likes: 24
    },
  ];
  
  // 더미 평점 분포 데이터 (0.5점 단위)
  const ratingDistribution = [
    { rating: 1, count: 5, percentage: 1.7 },
    { rating: 1.5, count: 8, percentage: 2.7 },
    { rating: 2, count: 12, percentage: 4 },
    { rating: 2.5, count: 18, percentage: 6 },
    { rating: 3, count: 30, percentage: 10 },
    { rating: 3.5, count: 45, percentage: 15 },
    { rating: 4, count: 75, percentage: 25 },
    { rating: 4.5, count: 60, percentage: 20 },
    { rating: 5, count: 47, percentage: 15.6 }
  ];
  
  const handleRatingChange = (rating: number) => {
    setUserRating(rating);
    setShowCommentForm(false);
  };
  
  const handleCommentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setUserComment(e.target.value);
  };
  
  const handleSubmitReview = () => {
    if (userRating === 0) {
      alert('평점을 선택해주세요.');
      return;
    }
    
    setIsSubmitting(true);
    
    // API 호출 대신 setTimeout으로 시뮬레이션
    setTimeout(() => {
      console.log('평가 제출:', { rating: userRating, comment: userComment });
      setIsSubmitting(false);
      setShowCommentForm(false);
      alert('평가가 등록되었습니다.');
    }, 1000);
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const handleReport = (reviewId: number) => {
    // 신고 기능 구현
    console.log('신고:', reviewId);
    setShowMoreMenu(null);
  };

  return (
    <section className={styles.section}>
      <h2 className={styles.sectionTitle}>웹툰 평가</h2>
      
      <div className={styles.ratingSummary}>
        <div className={styles.averageRating}>
          <div className={styles.ratingValue}>
            {webtoon.averageRating ? webtoon.averageRating?.toFixed(1) : '0.0'}
          </div>
          <div className={styles.ratingStars}>
            {[1, 2, 3, 4, 5].map(star => (
              <span 
                key={star} 
                className={`${styles.star} ${star <= Math.round(webtoon.averageRating) ? styles.filled : ''}`}
              >
                ★
              </span>
            ))}
          </div>
          <div className={styles.totalRatings}>
            총 {webtoon.totalRatings}명 평가
          </div>
        </div>

        <div className={styles.ratingDistribution}>
          <ResponsiveContainer width="100%" height={200}>
            <BarChart data={ratingDistribution}>
              <XAxis 
                dataKey="rating" 
                tickFormatter={(value) => `${value}점`}
                tick={{ fontSize: 12 }}
              />
              <YAxis hide />
              <Tooltip 
                formatter={(value: number) => [`${value}명`, '평가 수']}
                labelFormatter={(label) => `${label}점`}
              />
              <Bar 
                dataKey="count" 
                fill="#ffd700" 
                radius={[4, 4, 0, 0]}
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className={styles.userRating}>
        <h3 className={styles.userRatingTitle}>내 평가</h3>
        {state ? (
          <>
            <div className={styles.userRatingValue}>
              <div className={styles.ratingValue}>
                {userRating ? userRating.toFixed(1) : '0.0'}
              </div>
              <div className={styles.ratingStars}>
                {[1, 2, 3, 4, 5].map(star => (
                  <button
                    key={star}
                    className={`${styles.starButton} ${star <= userRating ? styles.selected : ''}`}
                    onClick={() => handleRatingChange(star)}
                    type="button"
                  >
                    ★
                  </button>
                ))}
              </div>
            </div>
            {userRating > 0 && !showCommentForm && (
              <button 
                className={styles.commentButton}
                onClick={() => setShowCommentForm(true)}
              >
                코멘트 남기기
              </button>
            )}
            {showCommentForm && (
              <div className={styles.commentForm}>
                <textarea
                  className={styles.commentTextarea}
                  value={userComment}
                  onChange={handleCommentChange}
                  placeholder="이 웹툰에 대한 의견을 남겨주세요."
                  rows={4}
                />
                <button
                  className={styles.submitButton}
                  onClick={handleSubmitReview}
                  disabled={isSubmitting}
                >
                  {isSubmitting ? '제출 중...' : '제출하기'}
                </button>
              </div>
            )}
          </>
        ) : (
          <div className={styles.loginPrompt}>
            로그인하여 평가를 남겨주세요.
          </div>
        )}
      </div>
      
      <div className={styles.reviewsSection}>
        <div className={styles.reviewsHeader}>
          <h3 className={styles.reviewsTitle}>사용자 평가</h3>
          <div className={styles.sortButtons}>
            <button 
              className={`${styles.sortButton} ${sortBy === 'popular' ? styles.active : ''}`}
              onClick={() => setSortBy('popular')}
            >
              인기순
            </button>
            <button 
              className={`${styles.sortButton} ${sortBy === 'recent' ? styles.active : ''}`}
              onClick={() => setSortBy('recent')}
            >
              최신순
            </button>
          </div>
        </div>
        
        {dummyReviews.length > 0 ? (
          <div className={styles.reviewsList}>
            {dummyReviews.map(review => (
              <div key={review.id} className={styles.reviewItem}>
                <div className={styles.reviewHeader}>
                  <div className={styles.reviewerInfo}>
                    <img 
                      src={review.profilePicture} 
                      alt={review.userName} 
                      className={styles.reviewerAvatar}
                    />
                    <div className={styles.reviewerDetails}>
                      <div className={styles.reviewerName}>{review.userName}</div>
                      <div className={styles.reviewDate}>{formatDate(review.createdAt)}</div>
                    </div>
                    <div className={styles.reviewRating}>
                      {[1, 2, 3, 4, 5].map(star => (
                        <span 
                          key={star} 
                          className={`${styles.reviewStar} ${star <= review.rating ? styles.filled : ''}`}
                        >
                          ★
                        </span>
                      ))}
                    </div>
                  </div>
                  <div className={styles.reviewActions}>
                    <button className={styles.likeButton}>
                      <FiThumbsUp />
                      <span className={styles.likeCount}>{review.likes}</span>
                    </button>
                    <div className={styles.moreButtonContainer}>
                      <button 
                        className={styles.moreButton}
                        onClick={() => setShowMoreMenu(review.id)}
                      >
                        <FiMoreVertical />
                      </button>
                      {showMoreMenu === review.id && (
                        <div className={styles.moreMenu}>
                          <button 
                            className={styles.menuItem}
                            onClick={() => handleReport(review.id)}
                          >
                            <FiFlag />
                            신고하기
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
                
                <div className={styles.reviewComment}>{review.comment}</div>
              </div>
            ))}
          </div>
        ) : (
          <div className={styles.noReviews}>아직 평가가 없습니다.</div>
        )}
      </div>
    </section>
  );
};

export default WebtoonRatingSection;