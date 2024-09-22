import React, { useContext, useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import ProfileWidget from './ProfileWidget';
import styles from './Header.module.css';
import { FiSearch, FiBell } from 'react-icons/fi';

const Header = () => {
  const navigate = useNavigate();
  const { isLoggedIn, logout } = useContext(AuthContext);
  const [isProfileWidgetOpen, setProfileWidgetOpen] = useState(false);
  const [widgetPosition, setWidgetPosition] = useState({ top: 0, right: 0 });
  const [isSearchInputVisible, setSearchInputVisible] = useState(false);
  const profileButtonRef = useRef(null);

  const handleLogout = async () => {
    logout();
    navigate('/');
  };

  const toggleProfileWidget = () => {
    if (!isProfileWidgetOpen) {
      const buttonRect = profileButtonRef.current.getBoundingClientRect();
      setWidgetPosition({
        top: buttonRect.bottom,
        right: window.innerWidth - buttonRect.right,
      });
    }
    setProfileWidgetOpen((prev) => !prev);
  };

  const toggleSearchInput = () => {
    setSearchInputVisible((prev) => !prev);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        profileButtonRef.current &&
        !profileButtonRef.current.contains(event.target)
      ) {
        setProfileWidgetOpen(false);
      }
    };

    window.addEventListener('click', handleClickOutside);
    return () => {
      window.removeEventListener('click', handleClickOutside);
    };
  }, []);

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
                type="text"
                placeholder="검색..."
                className={styles.searchInput}
                onKeyDown={(e) => {
                  if (e.key === 'Enter') {
                    navigate(`/search?query=${e.target.value}`);
                  }
                }}
              />
            )}
            <button onClick={toggleSearchInput} className={styles.iconButton}>
              <FiSearch className={styles.icon} color="white" size={24} />
            </button>

            <button className={styles.iconButton}>
              <FiBell className={styles.icon} color="white" size={24} /> 
            </button>
          </div>

          <div className={styles.profileContainer}>
            {isLoggedIn ? (
              <>
                <button
                  id="profileToggle"
                  ref={profileButtonRef}
                  onClick={toggleProfileWidget}
                  className={styles.profileButton}
                >
                  <img src="https://via.placeholder.com/40" alt="User Profile" className={styles.profilePicture} />
                </button>
                {isProfileWidgetOpen && (
                  <ProfileWidget
                    userProfilePic="https://via.placeholder.com/40"
                    userName="사용자 이름"
                    userEmail="email@example.com"
                    onNavigate={navigate}
                    onLogout={handleLogout}
                    widgetPosition={widgetPosition}
                  />
                )}
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
