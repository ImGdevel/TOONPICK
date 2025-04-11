import React, { useContext, useRef, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '@contexts/auth-context';
import { Routes as RoutePaths } from '@constants/routes';
import ProfileWidget from '@components/profile-widget';
import Menu from '@components/top-menu';
import Search from '@components/search-bar';
import NotificationComponent from '@components/notification';
import { FiSun, FiMoon } from 'react-icons/fi';
import styles from './style.module.css';

const Header: React.FC = () => {
  const navigate = useNavigate();
  const { isLoggedIn, logout, memberProfile } = useContext(AuthContext);
  const [isProfileWidgetOpen, setProfileWidgetOpen] = useState<boolean>(false);
  const [isDarkTheme, setIsDarkTheme] = useState<boolean>(false);
  const [isNotificationOpen, setNotificationOpen] = useState<boolean>(false);

  const profileButtonRef = useRef<HTMLButtonElement>(null);
  const profileWidgetRef = useRef<HTMLDivElement>(null);

  const handleLogout = async (): Promise<void> => {
    logout();
    navigate(RoutePaths.LOGIN);
  };

  const toggleTheme = (): void => {
    setIsDarkTheme((prev) => !prev);
    const theme = isDarkTheme ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', theme);
  };

  const toggleProfileWidget = (): void => {
    setProfileWidgetOpen((prev) => !prev);
  };

  const toggleNotification = (): void => {
    setNotificationOpen((prev) => !prev);
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

          <Link to={RoutePaths.WEBTOON_RATING_LIST} className={styles.navLink}>웹툰 평가</Link>


          <div className={styles.profileContainer}>
            {isLoggedIn ? (
              <>
                <button
                  id="profileToggle"
                  ref={profileButtonRef}
                  className={styles.profileButton}
                  onClick={toggleProfileWidget}
                >
                  <img
                    src={memberProfile?.profilePicture || "https://via.placeholder.com/40"}
                    alt="User Profile"
                    className={styles.profilePicture}
                  />
                </button>
                <ProfileWidget
                  memberProfile={memberProfile}
                  onNavigate={navigate}
                  onLogout={handleLogout}
                  isWidgetOpen={isProfileWidgetOpen}
                  setProfileWidgetOpen={setProfileWidgetOpen}
                  profileButtonRef={profileButtonRef}
                  profileWidgetRef={profileWidgetRef}
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
