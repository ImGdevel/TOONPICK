import React, { useState, useEffect } from 'react';
import styles from './MyProfilePage.module.css';
import MemberService from '@services/MemberService';
import WebtoonReviewService from '@services/WebtoonReviewService';
import WebtoonList from '@components/WebtoonList';
import ReviewList from '@components/ReviewList';
import MemberProfileSection from '@pages/MyProfilePage/MemberProfileSection';

import { Webtoon } from '@models/webtoon';
import { Review } from '@models/review';
import { MemberProfile } from '@models/member';

export interface MyPageState {
  memberProfile: MemberProfile | null;
  bookmarks: Webtoon[];
  favorites: Webtoon[];
  reviews: Review[];
  isLoading: boolean;
  error: string | null; 
}

const MyProfilePage: React.FC = () => {
  const [state, setState] = useState<MyPageState>({
    memberProfile: null,
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
          MemberService.getMemberProfile(),
          MemberService.getBookmarks(),
          MemberService.getFavorites(),
          WebtoonReviewService.getReviewsByWebtoon(0)
        ]);

        setState({
          memberProfile: profile.data || null,
          bookmarks: bookmarks.data || [],
          favorites: favorites.data || [],
          reviews: reviews.data || [],
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
      <MemberProfileSection memberProfile={state.memberProfile} />

      <section className={styles.bookmarks}>
        <h2>북마크</h2>
        <WebtoonList webtoons={state.bookmarks} />
      </section>

      <section className={styles.favorites}>
        <h2>좋아요</h2>
        <WebtoonList webtoons={state.favorites} />
      </section>

      <section className={styles.reviews}>
        <h2>내가 쓴 리뷰</h2>
        <ReviewList reviews={state.reviews} />
      </section>
    </div>
  );
};

export default MyProfilePage; 