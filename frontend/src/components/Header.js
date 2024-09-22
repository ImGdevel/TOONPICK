// src/components/Header.js
import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { AuthService } from '../services/AuthService';
import styles from './Header.module.css'; 

const Header = () => {
  const navigate = useNavigate();
  const { isLoggedIn, logout } = useContext(AuthContext);

  const handleNavigation = (path) => {
    navigate(path);
  };

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
      logout(); 
      navigate('/error');
    }
  };

  return (
    <header className={styles.header}>
      <div className={styles['header-title']}>
        <h1 onClick={handleHome}>TOONPICK</h1>

        <nav className={styles.menuBar}>
          <ul className={styles.menuList}>
            <li onClick={() => handleNavigation('/')} className={styles.menuItem}>홈</li>
            <li onClick={() => handleNavigation('/webtoon/ongoing')} className={styles.menuItem}>연재</li>
            <li onClick={() => handleNavigation('/webtoon/new')} className={styles.menuItem}>신작</li>
            <li onClick={() => handleNavigation('/webtoon/completed')} className={styles.menuItem}>완결</li>
            <li onClick={() => handleNavigation('/recommendation')} className={styles.menuItem}>추천</li>
          </ul>
        </nav>

        <input 
          type="text" 
          placeholder="검색..." 
          className={styles.searchInput}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              handleNavigation(`/search?query=${e.target.value}`);
            }
          }}
        />

        {/* Authentication Buttons */}
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
