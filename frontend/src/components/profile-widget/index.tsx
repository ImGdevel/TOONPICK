import React, { useEffect, useCallback } from 'react';
import { MemberProfile } from '@models/member';
import { Routes } from '@constants/routes';
import styles from './style.module.css';

interface ProfileWidgetProps {
  memberProfile: MemberProfile | null;
  onNavigate: (path: string) => void;
  onLogout: () => void;
  isWidgetOpen: boolean;
  setProfileWidgetOpen: (isOpen: boolean) => void;
  profileButtonRef: React.RefObject<HTMLButtonElement>;
  profileWidgetRef: React.RefObject<HTMLDivElement>;
}

const ProfileWidget: React.FC<ProfileWidgetProps> = ({
  memberProfile,
  onNavigate,
  onLogout,
  isWidgetOpen,
  setProfileWidgetOpen,
  profileButtonRef,
  profileWidgetRef,
}) => {
  const handleButtonClick = (action: () => void, closeWidget: () => void) => (event: React.MouseEvent) => {
    event.stopPropagation();
    action();
    closeWidget();
  };

  const closeProfileWidget = () => {
    setProfileWidgetOpen(false);
  };

  const handleClickOutside = useCallback((event: MouseEvent): void => {
    const isProfileClick = profileButtonRef.current && profileButtonRef.current.contains(event.target as Node);
    const isProfileWidgetClick = profileWidgetRef.current && profileWidgetRef.current.contains(event.target as Node);

    if (!isProfileClick && !isProfileWidgetClick) {
      closeProfileWidget();
    }
  }, [profileButtonRef, profileWidgetRef, setProfileWidgetOpen]);

  useEffect(() => {
    window.addEventListener('click', handleClickOutside);
    return () => {
      window.removeEventListener('click', handleClickOutside);
    };
  }, [handleClickOutside]);

  const defaultProfilePicture = '/image/profile/user.png';

  return (
    <div
      id="profileWidget"
      className={`${styles.profileWidget} ${isWidgetOpen ? styles.open : ''}`}
      style={{ pointerEvents: isWidgetOpen ? 'auto' : 'none' }}
      ref={profileWidgetRef}
    >
      <img 
        src={memberProfile?.profilePicture || defaultProfilePicture}
        alt="User Profile" 
        className={styles.widgetProfilePicture} 
      />
      <div className={styles.userInfo}>
        <p>{memberProfile?.nickname || '게스트 사용자'}</p>
      </div>
      <button
        onClick={handleButtonClick(() => onNavigate(Routes.USER_PROFILE), closeProfileWidget)}
        className={styles.widgetButton}
      >
        마이페이지
      </button>
      <button
        onClick={handleButtonClick(() => onNavigate(Routes.USER_PROFILE_EDIT), closeProfileWidget)}
        className={styles.widgetButton}
      >
        프로필 수정
      </button>
      <button
        onClick={handleButtonClick(() => onNavigate(Routes.HOME), closeProfileWidget)}
        className={styles.widgetButton}
      >
        나의 웹툰 리스트
      </button>
      <button
        onClick={handleButtonClick(onLogout, closeProfileWidget)}
        className={styles.widgetButton}
      >
        로그아웃
      </button>
    </div>
  );
};

export default ProfileWidget;
