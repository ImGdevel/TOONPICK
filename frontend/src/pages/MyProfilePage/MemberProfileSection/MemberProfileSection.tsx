import React from 'react';
import { MemberProfile } from '@models/member';
import styles from './MemberProfileSection.module.css';

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
            <p className={styles.level}>Level: {memberProfile.level}</p>
            <p className={styles.points}>Current Points: {memberProfile.points}</p>
            <div className={styles.stats}>
              <div className={styles.statItem}>
                <span>북마크한 웹툰: {memberProfile.bookmarkedWebtoons}</span>
              </div>
              <div className={styles.statItem}>
                <span>감상한 웹툰: {memberProfile.watchedWebtoons}</span>
              </div>
              <div className={styles.statItem}>
                <span>평가 남긴 웹툰: {memberProfile.ratedWebtoons}</span>
              </div>
            </div>
          </div>
        </div>
      )}
    </section>
  );
};

export default MemberProfileSection; 