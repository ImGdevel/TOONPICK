import React from 'react';
import { MemberProfile } from '@models/member';
import styles from './style.module.css';

interface ProfileCardProps {
  memberProfile: MemberProfile;
}

const ProfileCard: React.FC<ProfileCardProps> = ({ memberProfile }) => {
  if (!memberProfile) return null;

  return (
    <div className={styles.profileCard}>
      <div className={styles.profileHeader}>
        <img
          src={memberProfile.profileImage || '/image/profile/user.png'}
          alt="Profile"
          className={styles.profileImage}
        />
        <div className={styles.profileInfo}>
          <h1 className={styles.username}>{memberProfile.nickname}</h1>
          <div className={styles.levelBadge}>Level {memberProfile.level}</div>
        </div>
      </div>
      <div className={styles.achievements}>
        <div className={styles.achievementItem}>
          <span className={styles.achievementNumber}>{memberProfile.readWebtoons}</span>
          <span className={styles.achievementLabel}>읽은 웹툰</span>
        </div>
        <div className={styles.achievementItem}>
          <span className={styles.achievementNumber}>{memberProfile.reviewedWebtoons}</span>
          <span className={styles.achievementLabel}>리뷰한 웹툰</span>
        </div>
        <div className={styles.achievementItem}>
          <span className={styles.achievementNumber}>{memberProfile.collections}</span>
          <span className={styles.achievementLabel}>컬렉션</span>
        </div>
      </div>
      <div className={styles.badges}>
        {memberProfile.badges && memberProfile.badges.length > 0 ? (
          memberProfile.badges.map((badge) => (
            <div key={badge.id} className={styles.badge}>
              <img src={badge.icon} alt={badge.name} />
              <span>{badge.name}</span>
            </div>
          ))
        ) : (
          <div>뱃지가 없습니다.</div>
        )}
      </div>
    </div>
  );
};

export default ProfileCard;
