// src/components/Header.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Header.css';

const Header = () => {
  const navigate = useNavigate();

  const handleLogin = () => {
    navigate('/login');
  };

  const handleSignup = () => {
    navigate('/signup');
  };

  return (
    <header>
      <div className="header-title">
        <h1>TOONPICK</h1>
        <div className="auth-buttons">
          <button onClick={handleLogin}>로그인</button>
          <button onClick={handleSignup}>회원가입</button>
        </div>
      </div>
    </header>
  );
};

export default Header;
