// src/components/Header.js
import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import './Header.css';

const Header = () => {
  const navigate = useNavigate();
  const { isLoggedIn, logout } = useContext(AuthContext);

  const handleLogin = () => {
    navigate('/login');
  };

  const handleSignup = () => {
    navigate('/signup');
  };

  const handleMyPage = () => {
    navigate('/mypage');
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header>
      <div className="header-title">
        <h1>TOONPICK</h1>
        <div className="auth-buttons">
          {isLoggedIn ? (
            <>
              <button onClick={handleMyPage}>마이페이지</button>
              <button onClick={handleLogout}>로그아웃</button>
            </>
          ) : (
            <>
              <button onClick={handleLogin}>로그인</button>
              <button onClick={handleSignup}>회원가입</button>
            </>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
