// src/components/Header.js
import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { AuthService } from '../services/AuthService';
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

  const handleHome = () => {
    navigate('/');
  };

  const handleLogout = async () => {
    const result = await AuthService.logout();
    if (result.success) {
      logout(); 
      navigate('/');
    } else {
      navigate('/error');
    }
  };

  return (
    <header>
      <div className="header-title">
        <h1 onClick={handleHome}>TOONPICK</h1>
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
