import React from 'react';
import {  useAuth } from '@contexts/auth-context';
import styles from './style.module.css';

const ProfileIcon: React.FC = () => {
  const { state } = useAuth();
  
  return (
    <div className={styles.profileIcon}>
      <img
        src={state.memberProfile?.profileImage || '/image/profile/user.png'}
        alt="Profile"
        className={styles.profileImage}
      />
    </div>
  );
};

export default ProfileIcon;