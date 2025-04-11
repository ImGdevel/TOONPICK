import React, { useState } from 'react';
import { Webtoon } from '@models/webtoon';
// import { useAuth } from '@contexts/auth-context';
import styles from './style.module.css';
import { Review } from '@models/review';
import WebtoonReviewCard from '@components/webtoon-review-card';

interface WebtoonRatingSectionProps {
  webtoon: Webtoon;
}

const WebtoonRatingSection: React.FC<WebtoonRatingSectionProps> = ({ webtoon }) => {
  // const { isLoggedIn, memberProfile } = useAuth();
  const [userRating, setUserRating] = useState<number>(0);
  const [userComment, setUserComment] = useState<string>('');
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);


  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  
  // 더미 데이터
  const hasUserRated = isLoggedIn && Math.random() > 0.5; // 랜덤으로 평가 여부 결정
  
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
  
  // 더미 평점 분포 데이터
  const ratingDistribution = [
    { rating: 5, count: 120, percentage: 40 },
    { rating: 4, count: 90, percentage: 30 },
    { rating: 3, count: 45, percentage: 15 },
    { rating: 2, count: 30, percentage: 10 },
    { rating: 1, count: 15, percentage: 5 }
  ];
  
  // 사용자가 이미 평가한 경우 더미 데이터로 초기화
  React.useEffect(() => {
    if (isLoggedIn && hasUserRated) {
      setUserRating(4);
      setUserComment('이 웹툰은 정말 재미있습니다. 특히 캐릭터들의 성장 과정이 인상적이에요.');
    }
  }, [isLoggedIn, hasUserRated]);
  
  const handleRatingChange = (rating: number) => {
    setUserRating(rating);
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
  
  return (
    <section className={styles.section}>
      <h2 className={styles.sectionTitle}>웹툰 평가</h2>
      
      <div className={styles.ratingSummary}>
        <div className={styles.averageRating}>
          <div className={styles.ratingValue}>{webtoon.averageRating.toFixed(1)}</div>
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
          {ratingDistribution.map(item => (
            <div key={item.rating} className={styles.distributionItem}>
              <div className={styles.ratingLabel}>{item.rating}점</div>
              <div className={styles.distributionBar}>
                <div 
                  className={styles.distributionFill} 
                  style={{ width: `${item.percentage}%` }}
                />
              </div>
              <div className={styles.distributionCount}>
                {item.count}명 ({item.percentage}%)
              </div>
            </div>
          ))}
        </div>
      </div>
      
      {isLoggedIn && (
        <div className={styles.userRatingForm}>
          <h3 className={styles.formTitle}>
            {hasUserRated ? '내 평가 수정하기' : '내 평가 작성하기'}
          </h3>
          
          <div className={styles.ratingInput}>
            <div className={styles.ratingLabel}>평점</div>
            <div className={styles.starRating}>
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
          
          <div className={styles.commentInput}>
            <div className={styles.ratingLabel}>코멘트</div>
            <textarea
              className={styles.commentTextarea}
              value={userComment}
              onChange={handleCommentChange}
              placeholder="이 웹툰에 대한 의견을 남겨주세요."
              rows={4}
            />
          </div>
          
          <button
            className={styles.submitButton}
            onClick={handleSubmitReview}
            disabled={isSubmitting}
          >
            {isSubmitting ? '제출 중...' : (hasUserRated ? '평가 수정하기' : '평가 등록하기')}
          </button>
        </div>
      )}
      
      <div className={styles.reviewsSection}>
        <h3 className={styles.reviewsTitle}>사용자 평가</h3>
        
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
                
                <div className={styles.reviewComment}>{review.comment}</div>
                
                <div className={styles.reviewActions}>
                  <button className={styles.likeButton}>
                    <span className={styles.likeIcon}>👍</span>
                    <span className={styles.likeCount}>{review.likes}</span>
                  </button>
                </div>
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