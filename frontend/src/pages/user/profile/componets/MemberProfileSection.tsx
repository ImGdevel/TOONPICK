import React, { useState } from 'react';
import styles from './MemberProfileSection.module.css';
import { useNavigate } from 'react-router-dom';
import { MemberProfile } from '@models/member';
import { FaHeart, FaEdit, FaEllipsisH } from 'react-icons/fa';
import { Routes } from '@constants/routes';
import AchievementItem from '@components/achievement-item';
import LevelDisplay from '@components/level-display';

interface MemberProfileSectionProps {
  memberProfile: MemberProfile | null;
}

const MemberProfileSection: React.FC<MemberProfileSectionProps> = ({ memberProfile }) => {
  const navigate = useNavigate();
  const [isFollowing, setIsFollowing] = useState(false);
  const [followerCount, setFollowerCount] = useState(120); // 더미 데이터
  const [followingCount, setFollowingCount] = useState(150); // 더미 데이터
  const [recommendationCount, setRecommendationCount] = useState(30); // 더미 데이터
  const [reviewCount, setReviewCount] = useState(45); // 더미 데이터
  const [averageRating, setAverageRating] = useState(4.5); // 더미 데이터

  const toggleFollow = () => {
    setIsFollowing((prev) => !prev);
    setFollowerCount((prev) => (isFollowing ? prev - 1 : prev + 1));
  };

  const handleEditProfile = () => {
    // TODO: 프로필 수정 기능 추가
    navigate(Routes.USER_PROFILE_EDIT);
  };

  const handleMoreOptions = () => {
    // todo: 더보기 기능 추가
  };

  return (
    <section className={styles.profile}>
      {memberProfile && (
        <div className={styles.userInfo}>
          <img src={memberProfile.profilePicture} alt="프로필" className={styles.profilePicture} />
          <div className={styles.profileDetails}>
            <div className={styles.header}>
              <h3>{memberProfile.nickname}</h3>
              <div className={styles.actions}>
                <button onClick={toggleFollow} className={styles.iconButton}>
                  <FaHeart color={isFollowing ? 'red' : 'gray'} />
                </button>
                <button onClick={handleEditProfile} className={styles.iconButton}>
                  <FaEdit />
                </button>
                <button onClick={handleMoreOptions} className={styles.iconButton}>
                  <FaEllipsisH />
                </button>
              </div>
            </div>
            <LevelDisplay 
              currentLevel={memberProfile.level}
              currentPoints={memberProfile.points}
              maxPoints={100}
            />
            <div className={styles.achievements}>
              <AchievementItem title="웹툰 추천 수" count={recommendationCount} />
              <AchievementItem title="리뷰 작성 수" count={reviewCount} />
              <AchievementItem title="평균 평점" count={averageRating} />
            </div>
          </div>
        </div>
      )}
    </section>
  );
};

export default MemberProfileSection; 