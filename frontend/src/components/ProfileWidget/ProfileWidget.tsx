import React, { useEffect, useCallback } from 'react';
import styles from './ProfileWidget.module.css';

interface ProfileWidgetProps {
  userProfilePic: string;
  userName: string;
  userEmail: string;
  onNavigate: (path: string) => void;
  onLogout: () => void;
  isWidgetOpen: boolean;
  setProfileWidgetOpen: (isOpen: boolean) => void;
  profileButtonRef: React.RefObject<HTMLButtonElement>;
  profileWidgetRef: React.RefObject<HTMLDivElement>;
}

const ProfileWidget: React.FC<ProfileWidgetProps> = ({
  userProfilePic,
  userName,
  userEmail,
  onNavigate,
  onLogout,
  isWidgetOpen,
  setProfileWidgetOpen,
  profileButtonRef,
  profileWidgetRef,
}) => {
  const handleButtonClick = (action: () => void) => {
    action();
    setProfileWidgetOpen(false);
  };

  const handleClickOutside = useCallback((event: MouseEvent): void => {
    const isProfileClick = profileButtonRef.current && profileButtonRef.current.contains(event.target as Node);
    const isProfileWidgetClick = profileWidgetRef.current && profileWidgetRef.current.contains(event.target as Node);

    if (!isProfileClick && !isProfileWidgetClick) {
      setProfileWidgetOpen(false);
    }
  }, [profileButtonRef, profileWidgetRef, setProfileWidgetOpen]);

  useEffect(() => {
    window.addEventListener('click', handleClickOutside);
    return () => {
      window.removeEventListener('click', handleClickOutside);
    };
  }, [handleClickOutside]);

  return (
    <div
      id="profileWidget"
      className={`${styles['profile-widget']} ${isWidgetOpen ? styles['open'] : ''}`}
      style={{ pointerEvents: isWidgetOpen ? 'auto' : 'none' }}
    >
      <img src={userProfilePic} alt="User Profile" className={styles['widget-profile-picture']} />
      <div className={styles['user-info']}>
        <p>{userName}</p>
        <p>{userEmail}</p>
      </div>
      <button
        onClick={() => handleButtonClick(() => onNavigate('/mypage'))}
        className={styles['widget-button']}
      >
        마이페이지
      </button>
      <button
        onClick={() => handleButtonClick(() => onNavigate('/profile-edit'))}
        className={styles['widget-button']}
      >
        프로필 수정
      </button>
      <button
        onClick={() => handleButtonClick(() => onNavigate('/my-webtoons'))}
        className={styles['widget-button']}
      >
        나의 웹툰 리스트
      </button>
      <button
        onClick={() => handleButtonClick(onLogout)}
        className={styles['widget-button']}
      >
        로그아웃
      </button>
    </div>
  );
};

export default ProfileWidget;
