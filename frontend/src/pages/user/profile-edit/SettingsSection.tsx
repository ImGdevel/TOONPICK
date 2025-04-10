import React, { useState } from 'react';
import styles from './SettingsSection.module.css';

const SettingsSection: React.FC = () => {
  const [notificationsEnabled, setNotificationsEnabled] = useState(true);
  const [profileVisibility, setProfileVisibility] = useState('public');
  const [adultVerification, setAdultVerification] = useState(false);
  const [commentVisibility, setCommentVisibility] = useState('public');
  const [ratingVisibility, setRatingVisibility] = useState('public');
  const [webtoonPreferenceVisibility, setWebtoonPreferenceVisibility] = useState('public');
  const [recommendedWebtoonRange, setRecommendedWebtoonRange] = useState('all');
  const [hiddenUsers, setHiddenUsers] = useState([]);
  const [blockedUsers, setBlockedUsers] = useState([]);

  const handleSaveSettings = () => {
    // TODO: 설정 저장 로직 추가
    console.log('설정 저장:', {
      notificationsEnabled,
      profileVisibility,
      adultVerification,
      commentVisibility,
      ratingVisibility,
      webtoonPreferenceVisibility,
      recommendedWebtoonRange,
      hiddenUsers,
      blockedUsers,
    });
  };

  return (
    <div className={styles.settingsSection}>
      <h2>개인 설정</h2>
      <label>
        알림 설정:
        <input
          type="checkbox"
          checked={notificationsEnabled}
          onChange={() => setNotificationsEnabled((prev) => !prev)}
        />
      </label>
      <label>
        프로필 공개 범위:
        <select
          value={profileVisibility}
          onChange={(e) => setProfileVisibility(e.target.value)}
          className={styles.select}
        >
          <option value="public">공개</option>
          <option value="private">비공개</option>
        </select>
      </label>
      <label>
        성인 인증:
        <input
          type="checkbox"
          checked={adultVerification}
          onChange={() => setAdultVerification((prev) => !prev)}
        />
      </label>
      <label>
        댓글 공개 설정:
        <select
          value={commentVisibility}
          onChange={(e) => setCommentVisibility(e.target.value)}
          className={styles.select}
        >
          <option value="public">공개</option>
          <option value="private">비공개</option>
        </select>
      </label>
      <label>
        평가 공개 설정:
        <select
          value={ratingVisibility}
          onChange={(e) => setRatingVisibility(e.target.value)}
          className={styles.select}
        >
          <option value="public">공개</option>
          <option value="private">비공개</option>
        </select>
      </label>
      <label>
        웹툰 취향 공개 설정:
        <select
          value={webtoonPreferenceVisibility}
          onChange={(e) => setWebtoonPreferenceVisibility(e.target.value)}
          className={styles.select}
        >
          <option value="public">공개</option>
          <option value="private">비공개</option>
        </select>
      </label>
      <label>
        추천하는 웹툰 범위 설정:
        <select
          value={recommendedWebtoonRange}
          onChange={(e) => setRecommendedWebtoonRange(e.target.value)}
          className={styles.select}
        >
          <option value="all">모두</option>
          <option value="friends">친구만</option>
        </select>
      </label>
      <button onClick={handleSaveSettings} className={styles.saveButton}>
        설정 저장
      </button>
    </div>
  );
};

export default SettingsSection; 