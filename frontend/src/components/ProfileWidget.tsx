import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import styles from './ProfileWidget.module.css';

interface UserProfile {
  username: string;
  avatar?: string;
  favoriteCount: number;
}

const ProfileWidget: React.FC = () => {
  const { isLoggedIn } = useContext(AuthContext);
  const [profile, setProfile] = React.useState<UserProfile | null>(null);

  React.useEffect(() => {
    if (isLoggedIn) {
      // API 호출 또는 상태 관리 로직
      setProfile({
        username: "사용자",
        favoriteCount: 0
      });
    }
  }, [isLoggedIn]);

  if (!isLoggedIn || !profile) {
    return null;
  }

  return (
    <div className={styles.profileWidget}>
      <img
        src={profile.avatar || '/images/default-avatar.png'}
        alt="프로필 이미지"
        className={styles.avatar}
      />
      <div className={styles.info}>
        <span className={styles.username}>{profile.username}</span>
        <Link to="/favorites" className={styles.favorites}>
          즐겨찾기 ({profile.favoriteCount})
        </Link>
      </div>
    </div>
  );
};

export default ProfileWidget; 