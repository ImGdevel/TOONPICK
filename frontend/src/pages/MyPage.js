// src/components/MyPage.js
import React from 'react';
import styles from './MyPage.module.css'; // CSS 모듈로 가져오기

const MyPage = () => {
  const username = "사용자 닉네임"; // 이 부분은 실제 사용자 닉네임으로 대체
  const profileImageUrl = "https://via.placeholder.com/150"; // 프로필 이미지 URL (임시)

  return (
    <div className={styles['mypage-container']}>
      <div className={styles['mypage-header']}>
        <img className={styles['profile-image']} src={profileImageUrl} alt="Profile" />
        <h2 className={styles['username']}>{username}</h2>
      </div>
      <div className={styles['mypage-content']}>
        {/* 하단 부분은 나중에 채워넣을 내용 */}
      </div>
    </div>
  );
};

export default MyPage;
