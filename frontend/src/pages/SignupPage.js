// src/pages/SignupPage.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaGoogle, FaTwitter, FaApple } from 'react-icons/fa'; // 아이콘 추가
import { AuthService } from '../services/AuthService';
import './SignupPage.css';

const SignupPage = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState(null);

  const handleSignup = async () => {
    const result = await AuthService.signup(username, password, confirmPassword);

    if (result.success) {
      navigate('/login');
    } else {
      setError(result.message || 'Signup failed. Please try again.');
    }
  };

  return (
    <div className="signup-page">
      <div className="signup-box">
        <h1>TOONPICK</h1>
        <h3>회원가입</h3>
        <input
          type="text"
          placeholder="아이디(이메일)"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="password"
          placeholder="비밀번호 확인"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
        <button className="signup-button" onClick={handleSignup}>
          회원가입
        </button>
        {error && <p className="error-message">{error}</p>}

        <div className="social-buttons">
        <button className="social-button">
            <FaGoogle />
          </button>
          <button className="social-button">
            <FaTwitter />
          </button>
          <button className="social-button">
            <FaApple />
          </button>
        </div>
      </div>
    </div>
  );
};

export default SignupPage;
