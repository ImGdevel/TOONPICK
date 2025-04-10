import React, { useState } from 'react';
import styles from './ProfileSection.module.css';
import ProfilePictureUpload from './ProfilePictureUpload';

const ProfileSection: React.FC = () => {
  const [nickname, setNickname] = useState('사용자 닉네임');
  const [email, setEmail] = useState('user@example.com');
  const [password, setPassword] = useState('');
  const [profilePicture, setProfilePicture] = useState<File | null>(null);

  const handleSaveProfile = () => {
    // TODO: 프로필 저장 로직 추가
    console.log('프로필 저장:', { nickname, email, password, profilePicture });
  };

  return (
    <div className={styles.profileSection}>
      <h2>프로필 수정</h2>
      <ProfilePictureUpload onSave={setProfilePicture} />
      <label>
        닉네임:
        <input
          type="text"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
          className={styles.input}
        />
      </label>
      <label>
        이메일:
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className={styles.input}
        />
      </label>
      <label>
        비밀번호:
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className={styles.input}
        />
      </label>
      <button onClick={handleSaveProfile} className={styles.saveButton}>
        프로필 저장
      </button>
    </div>
  );
};

export default ProfileSection; 