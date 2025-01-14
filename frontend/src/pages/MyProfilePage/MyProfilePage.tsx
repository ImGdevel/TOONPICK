import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './MyProfilePage.module.css';
import MemberService from '@services/MemberService';
import WebtoonList from '@components/WebtoonList';
import MemberProfileSection from '@pages/MyProfilePage/MemberProfileSection';
import { MemberProfile } from '@models/member';
import { AuthContext } from '@/contexts/AuthContext';

import { Webtoon } from '@models/webtoon';
import Spinner from '@/components/Spinner';


export interface MyPageState {
  memberProfile: MemberProfile | null;
  bookmarks: Webtoon[];
  favorites: Webtoon[];
  isLoading: boolean;
  error: string | null; 
}

const MyProfilePage: React.FC = () => {
  const { isLoggedIn } = useContext(AuthContext);
  const navigate = useNavigate();
  const [state, setState] = useState<MyPageState>({
    memberProfile: null,
    bookmarks: [],
    favorites: [],
    isLoading: true,
    error: null
  });

  useEffect(() => {
    if (!isLoggedIn) {
      navigate('/login');
      return;
    }
    const fetchUserData = async () => {
      try {
        const [profile, bookmarks, favorites] = await Promise.all([
          MemberService.getMemberProfile(),
          MemberService.getFavorites(), // todo : 북마크로 변경할 것
          MemberService.getFavorites(),
        ]);

        console.log(profile.data);

        setState({
          memberProfile: profile.data || null,
          bookmarks: bookmarks.data || [],
          favorites: favorites.data || [],
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
  }, [isLoggedIn, navigate]);

  if (state.error) return <div>{state.error}</div>;

  return (
    <div className={styles.myPage}>

      {!state.isLoading ? (
          <div>
            <MemberProfileSection memberProfile={state.memberProfile} />
            <section className={styles.bookmarks}>
              <h2>북마크</h2>
              <WebtoonList webtoons={state.bookmarks} />
            </section>

            <section className={styles.favorites}>
              <h2>좋아요</h2>
              <WebtoonList webtoons={state.favorites} />
            </section>
          </div>
        ) : (
          <Spinner />
        )
      }
    </div>
  );
};

export default MyProfilePage; 
