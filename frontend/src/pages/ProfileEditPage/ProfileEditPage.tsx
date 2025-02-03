import React, { useState } from 'react';
import styles from './ProfileEditPage.module.css';
import ProfileSection from './ProfileSection';
import SettingsSection from './SettingsSection';

const ProfileEditPage: React.FC = () => {
  const [activeSection, setActiveSection] = useState('profile');

  return (
    <div className={styles.container}>
      <div className={styles.sidebar}>
        <h2>설정 메뉴</h2>
        <ul>
          <li
            className={activeSection === 'profile' ? styles.active : ''}
            onClick={() => setActiveSection('profile')}
          >
            프로필 수정
          </li>
          <li
            className={activeSection === 'settings' ? styles.active : ''}
            onClick={() => setActiveSection('settings')}
          >
            개인 설정
          </li>
        </ul>
      </div>
      <div className={styles.content}>
        {activeSection === 'profile' && <ProfileSection />}
        {activeSection === 'settings' && <SettingsSection />}
      </div>
    </div>
  );
};

export default ProfileEditPage; 