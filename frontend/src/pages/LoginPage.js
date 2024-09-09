// src/pages/LoginPage.js

import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaGoogle } from 'react-icons/fa';
import { AuthService } from '../services/AuthService';
import { AuthContext } from '../context/AuthContext';
import styles from './LoginPage.module.css'; // CSS 모듈로 가져오기

const LoginPage = () => {
  const navigate = useNavigate();
  const { login } = useContext(AuthContext);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleLogin = async () => {
    const result = await AuthService.login(username, password, login);

    if (result.success) {
      navigate('/');
    } else {
      setError(result.message || 'Login failed. Please try again.');
    }
  };

  const handleSocialLogin = (provider) => {
    AuthService.socialLogin(provider);
  };

  return (
    <div className={styles['login-page']}>
      <div className={styles['login-box']}>
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
        <button className={styles['login-button']} onClick={handleLogin}>
          로그인
        </button>
        {error && <p className={styles['error-message']}>{error}</p>}
        <div className={styles['social-buttons']}>
          <button
            className={styles['social-button']}
            onClick={() => handleSocialLogin('google')}
          >
            <FaGoogle /> Google 로그인
          </button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
