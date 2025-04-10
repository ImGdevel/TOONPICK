import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import styles from './style.module.css';

import WebtoonService from '@services/webtoon-service';
import WebtoonReviewService from '@services/webtoon-review-service';
import { Webtoon } from '@models/webtoon';
import { Review } from '@models/review';

import WebtoonDetailsSection from './sections/webtoon-details-section';
import WebtoonRatingSection from './sections/webtoon-rating-section';
import UserInteractionSection from './sections/member-interaction-section';

const WebtoonDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [webtoon, setWebtoon] = useState<Webtoon | null>(null);
  const [reviews, setReviews] = useState<Review[]>([]);
  const [averageRating, setAverageRating] = useState<number>(0);

  useEffect(() => {
    const fetchWebtoon = async () => {
      try {
        if (!id) return;
        
        const response = await WebtoonService.getWebtoonById(parseInt(id));
        
        if (response?.success && response.data) {
          setWebtoon(response.data);
          
          const reviewResponse = await WebtoonReviewService.getReviewsByWebtoon(parseInt(id));
          if (reviewResponse?.success && reviewResponse.data) {
            const totalRating = reviewResponse.data.reduce((acc, review) => acc + review.rating, 0);
            const avgRating = totalRating / reviewResponse.data.length || 0;
            setAverageRating(avgRating);
            setReviews(reviewResponse.data);
          }
        }
      } catch (error) {
        console.error('Error fetching webtoon data:', error);
      }
    };

    fetchWebtoon();
  }, [id]);

  const handleLike = async (reviewId: number) => {
    const response = await WebtoonReviewService.toggleLikeForReview(reviewId);
    if (response.success) {
      // 좋아요 상태 업데이트 로직 추가 필요
      // 예: 리뷰 목록에서 해당 리뷰의 좋아요 수를 업데이트
    }
  };

  const handleReport = (reviewId: number) => {
    // 리포트 처리 로직 추가 필요
    // 예: 리포트 모달 열기
  };

  if (!webtoon) {
    return <div>로딩중...</div>;
  }

  return (
    <div className={styles.webtoonDetailPage}>
      <WebtoonDetailsSection webtoon={webtoon} />
      <WebtoonRatingSection 
        averageRating={averageRating} 
        reviews={reviews} 
        onLike={handleLike} 
        onReport={handleReport} 
      />
      <UserInteractionSection webtoonId={webtoon.id} />
    </div>
  );
};

export default WebtoonDetailPage;