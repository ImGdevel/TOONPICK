import React, { useContext, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import ProfileWidget from './ProfileWidget';
import styles from './Header.module.css';

const Header = () => {
  const navigate = useNavigate();
  const { isLoggedIn, logout } = useContext(AuthContext);
  const [isProfileWidgetOpen, setProfileWidgetOpen] = useState(false);
  const [widgetPosition, setWidgetPosition] = useState({ top: 0, right: 0 });
  const [isSearchInputVisible, setSearchInputVisible] = useState(false);
  const profileButtonRef = useRef(null); // 프로필 버튼 위치를 참조하는 ref

  const handleNavigation = (path) => {
    navigate(path);
  };


  const handleLogout = async () => {
    logout();
    navigate('/');
  };

  const toggleProfileWidget = () => {
    if (!isProfileWidgetOpen) {
      // 버튼 위치 계산 후, 위젯이 열리기 전에 위치 설정
      const buttonRect = profileButtonRef.current.getBoundingClientRect();
      setWidgetPosition({
        top: buttonRect.bottom, // 버튼 바로 아래 위치
        right: window.innerWidth - buttonRect.right, // 버튼 오른쪽 맞춤
      });
    }
    setProfileWidgetOpen((prev) => !prev);
  };

  const toggleSearchInput = () => {
    setSearchInputVisible((prev) => !prev);
  };

  // 외부 클릭 시 위젯을 닫는 로직
  React.useEffect(() => {
    const handleClickOutside = (event) => {
      if (!profileButtonRef.current.contains(event.target)) {
        setProfileWidgetOpen(false);
      }
    };

    window.addEventListener('click', handleClickOutside);
    return () => {
      window.removeEventListener('click', handleClickOutside);
    };
  }, []);

  return (
    <header className={styles['header']}>
      <div className={styles['header-title']}>
        <h1 onClick={() => handleNavigation('/')}>TOONPICK</h1>

        <nav className={styles['menu-bar']}>
          <ul className={styles['menu-list']}>
            <li onClick={() => handleNavigation('/webtoon/ongoing')} className={styles['menu-item']}>연재</li>
            <li onClick={() => handleNavigation('/webtoon/new')} className={styles['menu-item']}>신작</li>
            <li onClick={() => handleNavigation('/webtoon/completed')} className={styles['menu-item']}>완결</li>
            <li onClick={() => handleNavigation('/recommendation')} className={styles['menu-item']}>추천</li>
          </ul>
        </nav>

      <div className={styles['right-section']}>

        <div className={styles['search-container']}>
              <button id="searchToggle" onClick={toggleSearchInput} className={styles['search-icon']}>
                {/* Search Icon */}
                <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 16l-4 4m0 0l4-4m-4 4V4m0 16h16" />
                </svg>
              </button>
              {isSearchInputVisible && (
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
              )}
            </div>



        <div className={styles['profile-container']}>
          {isLoggedIn ? (
            <>
              <button
                id="profileToggle"
                ref={profileButtonRef}
                onClick={toggleProfileWidget}
                className={styles['profile-button']}
              >
                <img src="https://via.placeholder.com/40" alt="User Profile" className={styles['profile-picture']} />
              </button>
              {isProfileWidgetOpen && (
                <ProfileWidget
                  id="profileWidget"
                  userProfilePic="https://via.placeholder.com/40"
                  userName="사용자 이름"
                  userEmail="email@example.com"
                  onNavigate={navigate}
                  onLogout={handleLogout}
                  widgetPosition={widgetPosition} // 미리 계산된 위치 전달
                />
              )}
            </>
          ) : (
            <button onClick={() => navigate('/login')} className={styles['login-button']}>로그인</button>
          )}
        </div>
      </div>
      </div>
    </header>
  );
};

export default Header;
