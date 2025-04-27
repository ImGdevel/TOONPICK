import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FiArrowLeft } from 'react-icons/fi';
import styles from './style.module.css';

interface NotificationSettings {
  webtoonUpdates: {
    enabled: boolean;
    frequency: 'instant' | 'daily' | 'weekly';
  };
  recommendations: {
    enabled: boolean;
    basedOnReadingHistory: boolean;
    basedOnRatings: boolean;
    basedOnSimilarUsers: boolean;
  };
  social: {
    comments: boolean;
    replies: boolean;
    likes: boolean;
    follows: boolean;
  };
  events: {
    promotions: boolean;
    newFeatures: boolean;
    surveys: boolean;
  };
  system: {
    maintenance: boolean;
    security: boolean;
  };
}

const NotificationSettingsPage: React.FC = () => {
  const navigate = useNavigate();
  const [settings, setSettings] = useState<NotificationSettings>({
    webtoonUpdates: {
      enabled: true,
      frequency: 'instant',
    },
    recommendations: {
      enabled: true,
      basedOnReadingHistory: true,
      basedOnRatings: true,
      basedOnSimilarUsers: true,
    },
    social: {
      comments: true,
      replies: true,
      likes: true,
      follows: true,
    },
    events: {
      promotions: true,
      newFeatures: true,
      surveys: false,
    },
    system: {
      maintenance: true,
      security: true,
    },
  });

  const handleToggle = <K extends keyof NotificationSettings>(
    category: K,
    setting: keyof NotificationSettings[K]
  ) => {
    setSettings(prev => ({
      ...prev,
      [category]: {
        ...prev[category],
        [setting]: !prev[category][setting],
      },
    }));
  };

  const handleFrequencyChange = (frequency: 'instant' | 'daily' | 'weekly') => {
    setSettings(prev => ({
      ...prev,
      webtoonUpdates: {
        ...prev.webtoonUpdates,
        frequency,
      },
    }));
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <button className={styles.backButton} onClick={() => navigate(-1)}>
          <FiArrowLeft size={24} />
        </button>
        <h1 className={styles.title}>알림 설정</h1>
      </div>

      <div className={styles.section}>
        <h2 className={styles.sectionTitle}>웹툰 업데이트</h2>
        <div className={styles.settingItem}>
          <div className={styles.settingLabel}>
            <span>업데이트 알림</span>
            <p className={styles.settingDescription}>
              구독 중인 웹툰의 업데이트를 알려드립니다.
            </p>
          </div>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.webtoonUpdates.enabled}
              onChange={() => handleToggle('webtoonUpdates', 'enabled')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
        {settings.webtoonUpdates.enabled && (
          <div className={styles.frequencySelector}>
            <label>
              <input
                type="radio"
                name="frequency"
                checked={settings.webtoonUpdates.frequency === 'instant'}
                onChange={() => handleFrequencyChange('instant')}
              />
              즉시 알림
            </label>
            <label>
              <input
                type="radio"
                name="frequency"
                checked={settings.webtoonUpdates.frequency === 'daily'}
                onChange={() => handleFrequencyChange('daily')}
              />
              일일 요약
            </label>
            <label>
              <input
                type="radio"
                name="frequency"
                checked={settings.webtoonUpdates.frequency === 'weekly'}
                onChange={() => handleFrequencyChange('weekly')}
              />
              주간 요약
            </label>
          </div>
        )}
      </div>

      <div className={styles.section}>
        <h2 className={styles.sectionTitle}>추천 알림</h2>
        <div className={styles.settingItem}>
          <div className={styles.settingLabel}>
            <span>추천 알림</span>
            <p className={styles.settingDescription}>
              맞춤 웹툰 추천을 받아보세요.
            </p>
          </div>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.recommendations.enabled}
              onChange={() => handleToggle('recommendations', 'enabled')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
        {settings.recommendations.enabled && (
          <div className={styles.subSettings}>
            <div className={styles.settingItem}>
              <span>읽은 웹툰 기반 추천</span>
              <label className={styles.switch}>
                <input
                  type="checkbox"
                  checked={settings.recommendations.basedOnReadingHistory}
                  onChange={() => handleToggle('recommendations', 'basedOnReadingHistory')}
                />
                <span className={styles.slider}></span>
              </label>
            </div>
            <div className={styles.settingItem}>
              <span>평가 기반 추천</span>
              <label className={styles.switch}>
                <input
                  type="checkbox"
                  checked={settings.recommendations.basedOnRatings}
                  onChange={() => handleToggle('recommendations', 'basedOnRatings')}
                />
                <span className={styles.slider}></span>
              </label>
            </div>
            <div className={styles.settingItem}>
              <span>유사 사용자 기반 추천</span>
              <label className={styles.switch}>
                <input
                  type="checkbox"
                  checked={settings.recommendations.basedOnSimilarUsers}
                  onChange={() => handleToggle('recommendations', 'basedOnSimilarUsers')}
                />
                <span className={styles.slider}></span>
              </label>
            </div>
          </div>
        )}
      </div>

      <div className={styles.section}>
        <h2 className={styles.sectionTitle}>소셜 알림</h2>
        <div className={styles.settingItem}>
          <span>댓글 알림</span>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.social.comments}
              onChange={() => handleToggle('social', 'comments')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
        <div className={styles.settingItem}>
          <span>답글 알림</span>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.social.replies}
              onChange={() => handleToggle('social', 'replies')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
        <div className={styles.settingItem}>
          <span>좋아요 알림</span>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.social.likes}
              onChange={() => handleToggle('social', 'likes')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
        <div className={styles.settingItem}>
          <span>팔로우 알림</span>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.social.follows}
              onChange={() => handleToggle('social', 'follows')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
      </div>

      <div className={styles.section}>
        <h2 className={styles.sectionTitle}>이벤트 알림</h2>
        <div className={styles.settingItem}>
          <span>프로모션 알림</span>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.events.promotions}
              onChange={() => handleToggle('events', 'promotions')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
        <div className={styles.settingItem}>
          <span>신규 기능 알림</span>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.events.newFeatures}
              onChange={() => handleToggle('events', 'newFeatures')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
        <div className={styles.settingItem}>
          <span>설문 조사 알림</span>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.events.surveys}
              onChange={() => handleToggle('events', 'surveys')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
      </div>

      <div className={styles.section}>
        <h2 className={styles.sectionTitle}>시스템 알림</h2>
        <div className={styles.settingItem}>
          <span>시스템 점검 알림</span>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.system.maintenance}
              onChange={() => handleToggle('system', 'maintenance')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
        <div className={styles.settingItem}>
          <span>보안 알림</span>
          <label className={styles.switch}>
            <input
              type="checkbox"
              checked={settings.system.security}
              onChange={() => handleToggle('system', 'security')}
            />
            <span className={styles.slider}></span>
          </label>
        </div>
      </div>
    </div>
  );
};

export default NotificationSettingsPage; 