import React, { useContext, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '@contexts/AuthContext';
import ProfileWidget from '@components/ProfileWidget';
import Menu from './Menu';
import Search from './Search';
import styles from './Header.module.css';

const Header: React.FC = () => {
  const navigate = useNavigate();
  const { isLoggedIn, logout, memberProfile } = useContext(AuthContext);
  const [isProfileWidgetOpen, setProfileWidgetOpen] = useState<boolean>(false);
  const [isDarkTheme, setIsDarkTheme] = useState<boolean>(false);

  const profileButtonRef = useRef<HTMLButtonElement>(null);
  const profileWidgetRef = useRef<HTMLDivElement>(null);

  const handleLogout = async (): Promise<void> => {
    logout();
    navigate('/login');
  };

  const toggleTheme = (): void => {
    setIsDarkTheme((prev) => !prev);
    const theme = isDarkTheme ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', theme);
  };

  const toggleProfileWidget = (): void => {
    setProfileWidgetOpen((prev) => !prev);
  };

  return (
    <header className={styles.header}>
      <div className={styles.headerTitle}>
        <h1 onClick={() => navigate('/')}>TOONPICK</h1>

        <Menu navigate={navigate} />

        <div className={styles.rightSection}>
          <Search navigate={navigate} isDarkTheme={isDarkTheme} toggleTheme={toggleTheme} />

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
              <button onClick={() => navigate('/login')} className={styles.loginButton}>Login</button>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
