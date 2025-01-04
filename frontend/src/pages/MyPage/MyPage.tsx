import React, { useState, useEffect } from 'react';
import { MyPageState } from '../../types/page';
import UserService from '@/services/UserService';
import WebtoonReviewService from '@/services/WebtoonReviewService';
import WebtoonGrid from '@/components/WebtoonGrid';
import ReviewList from '@/components/ReviewList';
import styles from './MyPage.module.css';

const MyPage: React.FC = () => {
  const [state, setState] = useState<MyPageState>({
    user: null,
    bookmarks: [],
    favorites: [],
    reviews: [],
    isLoading: true,
    error: null
  });

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const [profile, bookmarks, favorites, reviews] = await Promise.all([
          UserService.getUserProfile(),
          UserService.getBookmarks(),
          UserService.getFavorites(),
          WebtoonReviewService.getReviewsByWebtoon(0)
        ]);

        setState({
          user: profile.data,
          bookmarks: bookmarks.data,
          favorites: favorites.data,
          reviews: reviews.data,
          isLoading: false,
          error: null
        });
      } catch (error) {
        setState(prev => ({
          ...prev,
          isLoading: false,
          error: '사용자 데이터를 불러오는데 실패했습니다.'
        }));
      }
    };

    fetchUserData();
  }, []);

  if (state.isLoading) return <div>로딩중...</div>;
  if (state.error) return <div>{state.error}</div>;

  return (
    <div className={styles.myPage}>
      <section className={styles.profile}>
        <h2>프로필</h2>
        {state.user && (
          <div className={styles.userInfo}>
            <img src={state.user.profileImage} alt="프로필" />
            <h3>{state.user.username}</h3>
            <p>{state.user.email}</p>
          </div>
        )}
      </section>

      <section className={styles.bookmarks}>
        <h2>북마크</h2>
        <WebtoonGrid webtoons={state.bookmarks} />
      </section>

      <section className={styles.favorites}>
        <h2>좋아요</h2>
        <WebtoonGrid webtoons={state.favorites} />
      </section>

      <section className={styles.reviews}>
        <h2>내가 쓴 리뷰</h2>
        <ReviewList reviews={state.reviews} />
      </section>
    </div>
  );
};

export default MyPage; 