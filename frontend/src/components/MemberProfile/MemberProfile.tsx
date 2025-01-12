import React from 'react';
import { MemberProfile } from '@models/member';
import styles from './MemberProfile.module.css';

interface UserProfileProps {
  user: MemberProfile | null;
}

const UserProfile: React.FC<UserProfileProps> = ({ user }) => {
  
    console.log(user);
  
    return (
    <section className={styles.profile}>
      <h2>프로필</h2>
      {user && (
        <div className={styles.userInfo}>
          <img src={user.profilePicture} alt="프로필" />
          <div>
            <h3>{user.username}</h3>
          </div>
        </div>
      )}
    </section>
  );
};

export default UserProfile; 