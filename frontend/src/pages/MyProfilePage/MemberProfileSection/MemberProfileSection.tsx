import React from 'react';
import styles from './MemberProfileSection.module.css';
import LevelDisplay from '@components/LevelDisplay';
import AchievementItem from '@components/AchievementItem';
import { MemberProfile } from '@models/member';

interface MemberProfileSectionProps {
  memberProfile: MemberProfile | null;
}

const MemberProfileSection: React.FC<MemberProfileSectionProps> = ({ memberProfile }) => {
  return (
    <section className={styles.profile}>
      {memberProfile && (
        <div className={styles.userInfo}>
          <img src={memberProfile.profilePicture} alt="프로필" className={styles.profilePicture} />
          <div className={styles.profileDetails}>
            <h3>{memberProfile.nickname}</h3>
            <LevelDisplay 
              currentLevel={memberProfile.level}
              currentPoints={memberProfile.points}
              maxPoints={100}
            />
            <div className={styles.stats}>
              <AchievementItem 
                title="북마크한 웹툰" 
                count={memberProfile.bookmarkedWebtoons || 0} 
              />
              <AchievementItem 
                title="감상한 웹툰" 
                count={memberProfile.watchedWebtoons || 0} 
              />
              <AchievementItem 
                title="평가 남긴 웹툰" 
                count={memberProfile.ratedWebtoons || 0} 
              />
            </div>
          </div>
        </div>
      )}
    </section>
  );
};

export default MemberProfileSection; 