import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@contexts/auth-context';
import { Routes } from '@constants/routes';
import styles from './style.module.css';

import ProfileCard from './sections/profile-card';
import WebtoonListSection from './sections/webtoon-list';
import PreferenceCard from './sections/preference-card';
import ReviewSection from './sections/review-section';
import ReadingHistorySection from './sections/reading-history';
import AchievementSection from './sections/achievement';

import Spinner from '@components/spinner';

const UserProfilePage: React.FC = () => {
  const { state } = useAuth();
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!state.isAuthenticated) {
      navigate(Routes.LOGIN);
      return;
    }

    console.log(state.memberProfile)

    const fetchUserData = async () => {
      try {
        setIsLoading(true);
        // TODO: API 연동
        setIsLoading(false);
      } catch (error) {
        setError('사용자 데이터를 불러오는데 실패했습니다.');
        setIsLoading(false);
      }
    };

    fetchUserData();
  }, [state.isAuthenticated, navigate]);

  if (error) return <div className={styles.error}>{error}</div>;
  if (isLoading) return <Spinner />;

  return (
    <div className={styles.profilePage}>
      <div className={styles.mainContent}>
        {state.memberProfile ? (
          <>
            <ProfileCard memberProfile={state.memberProfile} />
            <div className={styles.gridLayout}>
              <div className={styles.leftColumn}>
                <WebtoonListSection
                  title="내가 보고 있는 웹툰"
                  webtoons={state.memberProfile?.readingHistory?.map((h: any) => h.webtoon) || []}
                  showMoreLink={Routes.READING_HISTORY}
                />

                <WebtoonListSection
                  title="내 명작 웹툰"
                  webtoons={state.memberProfile?.masterpieceWebtoons || []}
                  showMoreLink={Routes.MASTERPIECE_WEBTOONS}
                />

                <PreferenceCard
                  genrePreferences={state.memberProfile?.preferences?.genrePreferences || []}
                  emotionalTags={state.memberProfile?.preferences?.emotionalTags || []}
                  aiTags={state.memberProfile?.preferences?.aiTags || []}
                />
              </div>

              <div className={styles.rightColumn}>
                <ReviewSection
                  reviews={state.memberProfile?.reviews || []}
                  topReviews={state.memberProfile?.topReviews || []}
                />

                <ReadingHistorySection
                  readingHistory={state.memberProfile?.readingHistory || []}
                />

                <AchievementSection
                  badges={state.memberProfile?.badges || []}
                />
              </div>
            </div>
          </>
        ) : (
          <div className={styles.error}>프로필 정보를 불러오는 중입니다.</div>
        )}
      </div>
    </div>
  );
};

export default UserProfilePage; 
