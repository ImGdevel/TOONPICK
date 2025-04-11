import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '@contexts/auth-context';
import { Routes } from '@constants/routes';
import styles from './style.module.css';

import ProfileCard from './sections/profile-card';
import WebtoonListSection from './sections/webtoon-list';
import PreferenceCard from './sections/preference-card';
import ReviewSection from './sections/review-section';
import ReadingHistorySection from './sections/reading-history';
import AchievementSection from './sections/achievement';

import MemberService from '@services/member-service';
import Spinner from '@components/spinner';

const UserProfilePage: React.FC = () => {
  const { isLoggedIn, memberProfile } = useContext(AuthContext);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isLoggedIn) {
      navigate(Routes.LOGIN);
      return;
    }

    console.log(memberProfile)

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
  }, [isLoggedIn, navigate]);

  if (error) return <div className={styles.error}>{error}</div>;
  if (isLoading) return <Spinner />;

  return (
    <div className={styles.profilePage}>
      <div className={styles.mainContent}>
        {memberProfile ? (
          <>
            <ProfileCard memberProfile={memberProfile} />
            <div className={styles.gridLayout}>
              <div className={styles.leftColumn}>
                <WebtoonListSection
                  title="내가 보고 있는 웹툰"
                  webtoons={memberProfile?.readingHistory?.map(h => h.webtoon) || []}
                  showMoreLink={Routes.READING_HISTORY}
                />

                <WebtoonListSection
                  title="내 명작 웹툰"
                  webtoons={memberProfile?.masterpieceWebtoons || []}
                  showMoreLink={Routes.MASTERPIECE_WEBTOONS}
                />

                <PreferenceCard
                  genrePreferences={memberProfile?.preferences?.genrePreferences || []}
                  emotionalTags={memberProfile?.preferences?.emotionalTags || []}
                  aiTags={memberProfile?.preferences?.aiTags || []}
                />
              </div>

              <div className={styles.rightColumn}>
                <ReviewSection
                  reviews={memberProfile?.reviews || []}
                  topReviews={memberProfile?.topReviews || []}
                />

                <ReadingHistorySection
                  readingHistory={memberProfile?.readingHistory || []}
                />

                <AchievementSection
                  badges={memberProfile?.badges || []}
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
