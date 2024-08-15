// src/components/LoginPage.js
import React from 'react';
import { FaGoogle, FaTwitter, FaApple } from 'react-icons/fa'; // 아이콘 추가
import './LoginPage.css';

const LoginPage = () => {
  return (
    <div className="login-page">
      <div className="login-box">
        <h1>TOONPICK</h1>
        <h3>로그인</h3>
        <input type="text" placeholder="아이디" />
        <input type="password" placeholder="비밀번호" />
        <button className="login-button">로그인</button>
        <div className="social-buttons">
          <button className="social-button"></button>
          <button className="social-button"></button>
          <button className="social-button"></button>
          <button className="social-button"></button>
          <button className="social-button"></button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
