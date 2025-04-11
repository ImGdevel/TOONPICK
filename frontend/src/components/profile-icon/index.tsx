import React, { useContext } from 'react';
import { AuthContext } from '@contexts/auth-context';
import styles from './style.module.css';

const ProfileIcon: React.FC = () => {
  const { memberProfile } = useContext(AuthContext);

  return (
    <div className={styles.profileIcon}>
      <img
        src={memberProfile?.profilePicture || '/image/profile/user.png'}
        alt="Profile"
        className={styles.profileImage}
      />
    </div>
  );
};

export default ProfileIcon;