import React from 'react';
import { MemberProfile } from '@models/member';
import styles from './MemberProfileSection.module.css';

interface MemberProfileSectionProps {
  memberProfile: MemberProfile | null;
}

const MemberProfileSection: React.FC<MemberProfileSectionProps> = ({ memberProfile }) => {
  
    console.log(memberProfile);
  
    return (
    <section className={styles.profile}>
      <h2>프로필</h2>
      {memberProfile && (
        <div className={styles.userInfo}>
          <img src={memberProfile.profilePicture} alt="프로필" />
          <div>
            <h3>{memberProfile.nickname}</h3>
          </div>
        </div>
      )}
    </section>
  );
};

export default MemberProfileSection; 