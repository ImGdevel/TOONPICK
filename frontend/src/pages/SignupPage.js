// src/pages/SignupPage.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaGoogle, FaTwitter, FaApple } from 'react-icons/fa';
import { AuthService } from '../services/AuthService';
import styles from './SignupPage.module.css';

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
    <div className={styles['signup-page']}>
      <div className={styles['signup-box']}>
        <h1>TOONPICK</h1>
        <h3>회원가입</h3>
        <input
          type="text"
          placeholder="아이디(이메일)"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          className={styles['input']}
        />
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className={styles['input']}
        />
        <input
          type="password"
          placeholder="비밀번호 확인"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          className={styles['input']}
        />
        <button className={styles['signup-button']} onClick={handleSignup}>
          회원가입
        </button>
        {error && <p className={styles['error-message']}>{error}</p>}

        <div className={styles['social-buttons']}>
          <button className={styles['social-button']}>
            <FaGoogle />
          </button>
          <button className={styles['social-button']}>
            <FaTwitter />
          </button>
          <button className={styles['social-button']}>
            <FaApple />
          </button>
        </div>
      </div>
    </div>
  );
};

export default SignupPage;
