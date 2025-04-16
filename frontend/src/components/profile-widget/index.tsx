import React, { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@contexts/auth-context';
import { Routes } from '@constants/routes';
import { FiUser, FiSettings, FiBook, FiLogOut } from 'react-icons/fi';
import styles from './style.module.css';

interface ProfileWidgetProps {
  isOpen: boolean;
  onClose: () => void;
  buttonRef: React.RefObject<HTMLButtonElement>;
}

const ProfileWidget: React.FC<ProfileWidgetProps> = ({
  isOpen,
  onClose,
  buttonRef,
}) => {
  const navigate = useNavigate();
  const { state, logout } = useAuth();
  const widgetRef = React.useRef<HTMLDivElement>(null);

  const handleClickOutside = useCallback((event: MouseEvent) => {
    const isButtonClick = buttonRef.current?.contains(event.target as Node);
    const isWidgetClick = widgetRef.current?.contains(event.target as Node);

    if (!isButtonClick && !isWidgetClick) {
      onClose();
    }
  }, [buttonRef, onClose]);

  React.useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [handleClickOutside]);

  const handleAction = (action: () => void) => {
    action();
    onClose();
  };

  return (
    <div
      ref={widgetRef}
      className={`${styles.profileWidget} ${isOpen ? styles.open : ''}`}
    >
      <div className={styles.profileHeader}>
        <img
          src={state.memberProfile?.profileImage || '/image/profile/user.png'}
          alt="Profile"
          className={styles.profileImage}
        />
        <div className={styles.profileInfo}>
          <span className={styles.username}>{state.memberProfile?.nickname || '게스트'}</span>
          <span className={styles.email}>{state.memberProfile?.nickname || ''}</span>
        </div>
      </div>

      <div className={styles.menuList}>
        <button
          className={styles.menuItem}
          onClick={() => handleAction(() => navigate(Routes.USER_PROFILE))}
        >
          <FiUser className={styles.menuIcon} />
          <span>마이페이지</span>
        </button>
        <button
          className={styles.menuItem}
          onClick={() => handleAction(() => navigate(Routes.USER_PROFILE_EDIT))}
        >
          <FiSettings className={styles.menuIcon} />
          <span>프로필 설정</span>
        </button>
        <button
          className={styles.menuItem}
          onClick={() => handleAction(() => navigate(Routes.HOME))}
        >
          <FiBook className={styles.menuIcon} />
          <span>나의 웹툰</span>
        </button>
        <div className={styles.divider} />
        <button
          className={`${styles.menuItem} ${styles.logoutButton}`}
          onClick={() => handleAction(logout)}
        >
          <FiLogOut className={styles.menuIcon} />
          <span>로그아웃</span>
        </button>
      </div>
    </div>
  );
};

export default ProfileWidget;
