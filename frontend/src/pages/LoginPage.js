// src/pages/LoginPage.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaGoogle, FaTwitter, FaApple } from 'react-icons/fa'; // 아이콘 추가
import { AuthService } from '../services/AuthService';
import './LoginPage.css';

const LoginPage = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleLogin = async () => {
    const result = await AuthService.login(username, password);

    if (result.success) {
      navigate('/'); // 로그인 성공 시 홈으로 이동
    } else {
      setError(result.message || 'Login failed. Please try again.');
    }
  };

  return (
    <div className="login-page">
      <div className="login-box">
        <h1>TOONPICK</h1>
        <h3>로그인</h3>
        <input
          type="text"
          placeholder="아이디"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button className="login-button" onClick={handleLogin}>
          로그인
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

export default LoginPage;
