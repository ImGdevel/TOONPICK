import React, { useState, useEffect, useContext } from 'react';
import styles from './UserInteractionSection.module.css';
import WebtoonReviewService from '@services/WebtoonReviewService';
import UserService from '@services/UserService';
import { ReviewRequest } from '@models/review';
import StarRating from '@components/StarRating';
import BookmarkButton from '@components/BookMarkButton/BookmarkButton';
import { AuthContext } from '@contexts/AuthContext';

interface UserInteractionSectionProps {
  webtoonId: number;
}

const UserInteractionSection: React.FC<UserInteractionSectionProps> = ({ webtoonId }) => {
  const { isLoggedIn } = useContext(AuthContext);
  const [rating, setRating] = useState<number>(0);
  const [comment, setComment] = useState<string>('');
  const [isBookmarked, setIsBookmarked] = useState<boolean>(false);

  useEffect(() => {
    const checkIfBookmarked = async () => {
      if (!isLoggedIn) return;
      const response = await UserService.isFavoriteWebtoon(webtoonId);
      if (response.success) {
        setIsBookmarked(response.data ?? false);
      }
    };

    checkIfBookmarked();
  }, [webtoonId, isLoggedIn]);

  const handleSubmitReview = async () => {
    const reviewRequest: ReviewRequest = { webtoonId, rating, comment };
    const response = await WebtoonReviewService.createWebtoonReview(webtoonId, reviewRequest);
    if (response.success) {
      setRating(0);
      setComment('');
    } else {
      console.error(response.message);
    }
  };

  const toggleBookmark = async () => {
    if (!isLoggedIn) {
      console.warn('로그인 후 북마크를 추가할 수 있습니다.');
      return;
    }

    const response = isBookmarked
      ? await UserService.removeFavoriteWebtoon(webtoonId)
      : await UserService.addFavoriteWebtoon(webtoonId);

    if (response.success) {
      setIsBookmarked(prev => !prev);
    } else {
      console.error(response.message);
    }
  };

  return (
    <div className={styles.interactionSection}>
      <h3>상호작용</h3>
      <div className={styles.reviewForm}>
        <h4>웹툰 평가 작성</h4>
        <StarRating 
          rating={rating} 
          onChange={setRating} 
          interactive={true} 
        />
        <div>
          <label>코멘트:</label>
          <textarea value={comment} onChange={(e) => setComment(e.target.value)} />
        </div>
        <button onClick={handleSubmitReview}>리뷰 작성</button>
      </div>
      <div className={styles.bookmarkSection}>
        <BookmarkButton 
          isBookmarked={isBookmarked} 
          onClick={toggleBookmark} 
        />
      </div>
    </div>
  );
};

export default UserInteractionSection; 