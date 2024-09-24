import React, { useContext, useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import ProfileWidget from './ProfileWidget';
import styles from './Header.module.css';
import { FiSearch, FiBell } from 'react-icons/fi';
import { getUserProfile } from '../services/UserService';

const Header = () => {
  const navigate = useNavigate();
  const { isLoggedIn, logout } = useContext(AuthContext);
  const [isProfileWidgetOpen, setProfileWidgetOpen] = useState(false);
  const [widgetPosition, setWidgetPosition] = useState({ top: 0, right: 0 });
  const [isSearchInputVisible, setSearchInputVisible] = useState(false);
  const [userProfile, setUserProfile] = useState(null); 
  const profileButtonRef = useRef(null);
  const searchInputRef = useRef(null);

  useEffect(() => {
    const fetchUserProfile = async () => {
      if (isLoggedIn) {
        const result = await getUserProfile();
        console.log(result)
        if (result.success) {
          setUserProfile(result.data);
        } else {
          console.error(result.message);
        }
      }
    };

    fetchUserProfile();
  }, [isLoggedIn]);

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

  const toggleSearchInput = (event) => {
    event.stopPropagation();
    setSearchInputVisible((prev) => !prev);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      const isProfileClick = profileButtonRef.current && profileButtonRef.current.contains(event.target);
      const isSearchClick = searchInputRef.current && searchInputRef.current.contains(event.target);
      
      if (!isProfileClick && isSearchClick) {
        setProfileWidgetOpen(false);
      } else if (!isSearchClick && isProfileClick) {
        setSearchInputVisible(false);
      } else if (!isProfileClick && !isSearchClick) {
        setProfileWidgetOpen(false);
        setSearchInputVisible(false);
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
                ref={searchInputRef}
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
                  <img src={userProfile?.profilePic || "https://via.placeholder.com/40"} alt="User Profile" className={styles.profilePicture} />
                </button>
                {isProfileWidgetOpen && userProfile && (
                  <ProfileWidget
                    userProfilePic={userProfile.profilePic || "https://via.placeholder.com/40"}
                    userName={userProfile.username || "사용자 이름"}
                    userEmail={userProfile.email || "email@example.com"}
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
