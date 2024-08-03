// src/components/SignupPage.js
import React from 'react';
import './SignupPage.css';

const SignupPage = () => {
  return (
    <div className="signup-page">
      <div className="signup-box">
        <h2>회원가입</h2>
        <input type="text" placeholder="아이디" />
        <input type="password" placeholder="비밀번호" />
        <input type="password" placeholder="비밀번호 확인" />
        <input type="email" placeholder="이메일" />
        <button className="signup-button">회원가입</button>
        <h3>소셜 회원가입</h3>
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

export default SignupPage;
