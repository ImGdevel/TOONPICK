// src/components/LoginPage.js
import React from 'react';
import './LoginPage.css';

const LoginPage = () => {
  return (
    <div className="login-page">
      <div className="login-box">
        <h2>로그인</h2>
        <input type="text" placeholder="아이디" />
        <input type="password" placeholder="비밀번호" />
        <button className="login-button">로그인</button>
        <h3>소셜 로그인</h3>
        <div className="social-buttons">
          <button className="social-button">Google</button>
          <button className="social-button">Facebook</button>
          <button className="social-button">Twitter</button>
          <button className="social-button">Kakao</button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
