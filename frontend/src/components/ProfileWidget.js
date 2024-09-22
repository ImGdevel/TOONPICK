// src/components/ProfileWidget.js
import React from 'react';
import styles from './ProfileWidget.module.css';

const ProfileWidget = ({ userProfilePic, userName, userEmail, onNavigate, onLogout }) => {
  return (
    <div className={styles['profile-widget']}>
      <img src={userProfilePic} alt="User Profile" className={styles['widget-profile-picture']} />
      <div className={styles['user-info']}>
        <p>{userName}</p>
        <p>{userEmail}</p>
      </div>
      <button onClick={() => onNavigate('/mypage')} className={styles['widget-button']}>마이페이지</button>
      <button onClick={() => onNavigate('/profile-edit')} className={styles['widget-button']}>프로필 수정</button>
      <button onClick={() => onNavigate('/my-webtoons')} className={styles['widget-button']}>나의 웹툰 리스트</button>
      <button onClick={onLogout} className={styles['widget-button']}>로그아웃</button>
    </div>
  );
};

export default ProfileWidget;
