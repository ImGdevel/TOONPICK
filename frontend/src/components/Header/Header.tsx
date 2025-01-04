import React, { useContext, useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '@/contexts/AuthContext';
import ProfileWidget from '@/components/ProfileWidget';
import styles from './Header.module.css';
import { FiSearch, FiBell, FiSun, FiMoon } from 'react-icons/fi';

const Header: React.FC = () => {
  const navigate = useNavigate();
  const { isLoggedIn, logout } = useContext(AuthContext);
  const [isProfileWidgetOpen, setProfileWidgetOpen] = useState<boolean>(false);
  const [isSearchInputVisible, setSearchInputVisible] = useState<boolean>(false);
  const [isDarkTheme, setIsDarkTheme] = useState<boolean>(false);

  const profileButtonRef = useRef<HTMLButtonElement>(null);
  const profileWidgetRef = useRef<HTMLDivElement>(null);
  const searchInputRef = useRef<HTMLInputElement>(null);

  const handleLogout = async (): Promise<void> => {
    logout();
    navigate('/');
  };

  const toggleSearchInput = (event: React.MouseEvent): void => {
    event.stopPropagation();
    setSearchInputVisible((prev) => !prev);
  };

  const toggleTheme = (): void => {
    setIsDarkTheme((prev) => !prev);
    const theme = isDarkTheme ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', theme);
    
    // 테마 CSS 파일 로드
    const linkElement = document.createElement('link');
    linkElement.rel = 'stylesheet';
    linkElement.href = '/styles/theme.css';
    document.head.appendChild(linkElement);
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent): void => {
      const isProfileClick = profileButtonRef.current && profileButtonRef.current.contains(event.target as Node);
      const isSearchClick = searchInputRef.current && searchInputRef.current.contains(event.target as Node);
      const isProfileWidgetClick = profileWidgetRef.current && profileWidgetRef.current.contains(event.target as Node);

      if (!isProfileClick && !isProfileWidgetClick) {
        setProfileWidgetOpen(false);
      }

      if (!isSearchClick) {
        setSearchInputVisible(false);
      }
    };

    window.addEventListener('click', handleClickOutside);
    return () => {
      window.removeEventListener('click', handleClickOutside);
    };
  }, []);

  const handleMouseEnterProfile = (): void => {
    setProfileWidgetOpen(true);
  };

  const handleMouseLeaveProfile = (): void => {
    setProfileWidgetOpen(false);
  };

  return (
    <header className={styles.header}>
      <div className={styles.headerTitle}>
        <h1 onClick={() => navigate('/')}>TOONPICK</h1>

        <nav className={styles.menuBar}>
          <ul className={styles.menuList}>
            <li onClick={() => navigate('/webtoon/ongoing')} className={styles.menuItem}>연재</li>
            <li onClick={() => navigate('/webtoon/new')} className={styles.menuItem}>신작</li>
            <li onClick={() => navigate('/webtoon/completed')} className={styles.menuItem}>완결</li>
            <li onClick={() => navigate('/recommendation')} className={styles.menuItem}>추천</li>
          </ul>
        </nav>

        <div className={styles.rightSection}>
          <div className={styles.searchContainer}>
            {isSearchInputVisible && (
              <input
                ref={searchInputRef}
                type="text"
                placeholder="검색..."
                className={styles.searchInput}
                onKeyDown={(e: React.KeyboardEvent<HTMLInputElement>) => {
                  if (e.key === 'Enter') {
                    navigate(`/search?query=${e.currentTarget.value}`);
                  }
                }}
              />
            )}
            <button onClick={toggleSearchInput} className={styles.iconButton}>
              <FiSearch className={styles.icon} color="white" size={24} />
            </button>

            <button onClick={toggleTheme} className={styles.iconButton}>
              {isDarkTheme ? (
                <FiSun className={styles.icon} color="white" size={24} />
              ) : (
                <FiMoon className={styles.icon} color="white" size={24} />
              )}
            </button>

            <button className={styles.iconButton}>
              <FiBell className={styles.icon} color="white" size={24} />
            </button>
          </div>

          <div
            className={styles.profileContainer}
            onMouseEnter={handleMouseEnterProfile}
            onMouseLeave={handleMouseLeaveProfile}
          >
            {isLoggedIn ? (
              <>
                <button
                  id="profileToggle"
                  ref={profileButtonRef}
                  className={styles.profileButton}
                >
                  <img src="https://via.placeholder.com/40" alt="User Profile" className={styles.profilePicture} />
                </button>
                <ProfileWidget
                  userProfilePic="https://via.placeholder.com/40"
                  userName="사용자 이름"
                  userEmail="email@example.com"
                  onNavigate={navigate}
                  onLogout={handleLogout}
                  isWidgetOpen={isProfileWidgetOpen}
                  setProfileWidgetOpen={setProfileWidgetOpen}
                />
              </>
            ) : (
              <button onClick={() => navigate('/login')} className={styles.loginButton}>Login</button>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
