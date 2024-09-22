// src/components/Header.js
import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { AuthService } from '../services/AuthService';
import ProfileWidget from './ProfileWidget';
import styles from './Header.module.css'; 

const Header = () => {
  const navigate = useNavigate();
  const { isLoggedIn, logout } = useContext(AuthContext);
  const [isProfileWidgetOpen, setProfileWidgetOpen] = useState(false);

  const handleNavigation = (path) => {
    navigate(path);
  };

  const handleLogin = () => {
    navigate('/login');
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

  const toggleProfileWidget = () => {
    setProfileWidgetOpen((prev) => !prev);
  };

  return (
    <header className={styles['header']}>
      <div className={styles['header-title']}>
        <h1 onClick={handleHome}>TOONPICK</h1>

        {/* Navigation Menu */}
        <nav className={styles['menu-bar']}>
          <ul className={styles['menu-list']}>
            <li onClick={() => handleNavigation('/webtoon/ongoing')} className={styles['menu-item']}>연재</li>
            <li onClick={() => handleNavigation('/webtoon/new')} className={styles['menu-item']}>신작</li>
            <li onClick={() => handleNavigation('/webtoon/completed')} className={styles['menu-item']}>완결</li>
            <li onClick={() => handleNavigation('/recommendation')} className={styles['menu-item']}>추천</li>
          </ul>
        </nav>

        {/* Search Input and Auth Buttons */}
        <div className={styles['right-section']}>
          <input 
            type="text" 
            placeholder="검색..." 
            className={styles['search-input']}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                handleNavigation(`/search?query=${e.target.value}`);
              }
            }}
          />
          <div className={styles['auth-buttons']}>
            {isLoggedIn ? (
              <>
                <img 
                  src="path_to_user_profile_picture" 
                  alt="User Profile" 
                  className={styles['profile-picture']} 
                  onClick={toggleProfileWidget} 
                />
                {isProfileWidgetOpen && (
                  <ProfileWidget 
                    userProfilePic="path_to_user_profile_picture" 
                    userName="사용자 이름" 
                    userEmail="이메일 주소" 
                    onNavigate={handleNavigation} 
                    onLogout={handleLogout} 
                  />
                )}
              </>
            ) : (
              <button onClick={handleLogin}>로그인</button>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
