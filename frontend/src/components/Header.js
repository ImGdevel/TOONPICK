// src/components/Header.js
import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { AuthService } from '../services/AuthService';
import styles from './Header.module.css'; // CSS 모듈로 가져오기

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
      {/* CSS 모듈을 사용할 때, 클래스 이름 앞에 styles를 붙여야 합니다. */}
      <div className={styles['header-title']}>
        <h1 onClick={handleHome}>TOONPICK</h1>
        <div className={styles['auth-buttons']}>
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
