// src/components/MyPage.js
import React from 'react';
import './MyPage.css';

const MyPage = () => {
  const username = "사용자 닉네임"; // 이 부분은 실제 사용자 닉네임으로 대체
  const profileImageUrl = "https://via.placeholder.com/150"; // 프로필 이미지 URL (임시)

  return (
    <div className="mypage-container">
      <div className="mypage-header">
        <img className="profile-image" src={profileImageUrl} alt="Profile" />
        <h2 className="username">{username}</h2>
      </div>
      <div className="mypage-content">
        {/* 하단 부분은 나중에 채워넣을 내용 */}
      </div>
    </div>
  );
};

export default MyPage;
