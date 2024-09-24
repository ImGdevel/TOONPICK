// src/components/MyPage.js
import React, { useState, useEffect } from 'react';
import { getUserProfile } from '../services/UserService';
import styles from './MyPage.module.css'; 

const MyPage = () => {
  const [userProfile, setUserProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUserProfile = async () => {
      const response = await getUserProfile();
      if (response.success) {
        setUserProfile(response.data);
      } else {
        console.error(response.message);
      }
      setLoading(false);
    };

    fetchUserProfile();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!userProfile) {
    return <div>User profile not found.</div>;
  }

  return (
    <div className={styles['mypage-container']}>
      <div className={styles['profile-section']}>
        <div className={styles['profile-card']}>
          <img className={styles['profile-image']} src={userProfile.profilePicture || "https://via.placeholder.com/150"} alt="Profile" />
          <div className={styles['profile-details']}>
            <div className={styles['profile-header']}>
              <span className={styles['username']}>{userProfile.username}</span>
              <button className={styles['edit-button']}>프로필 수정</button>
            </div>
            <div className={styles['email']}>{userProfile.email}</div>
            <div className={styles['badge-cards']}>
              <div className={styles['badge-card']}>
                <h3>보고있는 웹툰</h3>
                <p>{userProfile.currentlyWatchingCount}편</p>
              </div>
              <div className={styles['badge-card']}>
                <h3>관심 등록한 웹툰</h3>
                <p>{userProfile.favoriteCount}편</p>
              </div>
              <div className={styles['badge-card']}>
                <h3>평가를 남긴 웹툰</h3>
                <p>{userProfile.ratedCount}편</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className={styles['content-section']}>
        {/* 하단 콘텐츠 섹션 */}
      </div>
    </div>
  );
};

export default MyPage;
