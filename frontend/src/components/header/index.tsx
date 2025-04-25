import React, { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@contexts/auth-context';
import { Routes as RoutePaths } from '@constants/routes';
import ProfileWidget from '@components/profile-widget';
import Menu from '@components/top-menu';
import Search from '@components/search-bar';
import NotificationComponent from '@components/notification';
import { FiSun, FiMoon } from 'react-icons/fi';
import styles from './style.module.css';
import ProfileIcon from '@components/profile-icon';

const Header: React.FC = () => {
  const navigate = useNavigate();
  const { state } = useAuth();
  const [isDarkTheme, setIsDarkTheme] = useState<boolean>(false);
  const [isProfileOpen, setIsProfileOpen] = useState(false);

  const profileButtonRef = useRef<HTMLButtonElement>(null);

  const toggleTheme = (): void => {
    setIsDarkTheme((prev) => !prev);
    const theme = isDarkTheme ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', theme);
  };

  const toggleProfile = () => {
    setIsProfileOpen(prev => !prev);
  };

  return (
    <header className={styles.header}>
      <div className={styles.headerTitle}>
        <h1 onClick={() => navigate(RoutePaths.HOME)}>TOONPICK</h1>

        <Menu navigate={navigate} />

        <div className={styles.rightSection}>
          <Search navigate={navigate} />

          <button onClick={toggleTheme} className={styles.iconButton}>
            {isDarkTheme ? (
              <FiSun className={styles.icon} color="white" size={24} />
            ) : (
              <FiMoon className={styles.icon} color="white" size={24} />
            )}
          </button>

          <NotificationComponent />

        {/* <Link to={RoutePaths.WEBTOON_RATING_LIST} className={styles.navLink}>웹툰 평가</Link> */}

          <div className={styles.profileContainer}>
            {state.isAuthenticated ? (
              <>
                <button
                  ref={profileButtonRef}
                  className={styles.profileButton}
                  onClick={toggleProfile}
                >
                  <ProfileIcon />
                </button>
                <ProfileWidget
                  isOpen={isProfileOpen}
                  onClose={() => setIsProfileOpen(false)}
                  buttonRef={profileButtonRef}
                />
              </>
            ) : (
              <button onClick={() => navigate(RoutePaths.LOGIN)} className={styles.loginButton}>Login</button>
            )}
          </div>

        </div>
      </div>
    </header>
  );
};

export default Header;
