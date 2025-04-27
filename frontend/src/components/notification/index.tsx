import React, { useState, useRef, useEffect, useCallback } from 'react';
import { FiBell, FiSettings } from 'react-icons/fi';
import { useNavigate } from 'react-router-dom';
import { Routes as RoutePaths } from '@constants/routes';
import styles from './style.module.css';

interface Notification {
  id: number;
  type: 'recommendation' | 'update' | 'comment' | 'rating';
  message: string;
  webtoonId?: number;
  isRead: boolean;
  createdAt: string;
}

const NotificationComponent: React.FC = () => {
  const navigate = useNavigate();
  const [isOpen, setIsOpen] = useState(false);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const notificationRef = useRef<HTMLDivElement>(null);

  // 더미 알림 데이터
  useEffect(() => {
    setNotifications([
      {
        id: 1,
        type: 'recommendation',
        message: '새로운 웹툰 추천이 도착했습니다!',
        webtoonId: 123,
        isRead: false,
        createdAt: '2024-03-20T10:00:00'
      },
      {
        id: 2,
        type: 'update',
        message: '구독 중인 웹툰이 업데이트 되었습니다.',
        webtoonId: 456,
        isRead: false,
        createdAt: '2024-03-20T09:30:00'
      },
      {
        id: 3,
        type: 'update',
        message: '구독 중인 웹툰이 업데이트 되었습니다.',
        webtoonId: 45622,
        isRead: false,
        createdAt: '2024-03-20T09:30:00'
      },
      {
        id: 4,
        type: 'update',
        message: '구독 중인 웹툰이 업데이트 되었습니다.',
        webtoonId: 45622,
        isRead: false,
        createdAt: '2024-03-20T09:30:00'
      },
      {
        id: 5,
        type: 'update',
        message: '구독 중인 웹툰이 업데이트 되었습니다.',
        webtoonId: 45622,
        isRead: false,
        createdAt: '2024-03-20T09:30:00'
      }

    ]);
  }, []);

  const unreadCount = notifications.filter(n => !n.isRead).length;

  const handleNotificationClick = (notification: Notification) => {
    if (notification.webtoonId) {
      navigate(`/webtoon/${notification.webtoonId}`);
    }
    setNotifications(prev => 
      prev.map(n => 
        n.id === notification.id ? { ...n, isRead: true } : n
      )
    );
    setIsOpen(false);
  };

  const handleClickOutside = useCallback((event: MouseEvent): void => {
    const isNotificationClick = notificationRef.current && notificationRef.current.contains(event.target as Node);

    if (!isNotificationClick) {
      setIsOpen(false);
    }
  }, []);

  useEffect(() => {
    window.addEventListener('click', handleClickOutside);
    return () => {
      window.removeEventListener('click', handleClickOutside);
    };
  }, [handleClickOutside]);

  return (
    <div className={styles.notificationContainer} ref={notificationRef}>
      <button 
        className={styles.notificationButton}
        onClick={() => setIsOpen(!isOpen)}
      >
        <FiBell className={styles.icon} size={24} />
        {unreadCount > 0 && (
          <span className={styles.badge}>{unreadCount}</span>
        )}
      </button>

      {isOpen && (
        <div className={styles.notificationDropdown}>
          <div className={styles.notificationList}>
            {notifications.length > 0 ? (
              notifications.map(notification => (
                <div
                  key={notification.id}
                  className={`${styles.notificationItem} ${!notification.isRead ? styles.unread : ''}`}
                  onClick={() => handleNotificationClick(notification)}
                >
                  <p className={styles.message}>{notification.message}</p>
                  <span className={styles.time}>
                    {new Date(notification.createdAt).toLocaleString()}
                  </span>
                </div>
              ))
            ) : (
              <div className={styles.emptyMessage}>알림이 없습니다.</div>
            )}
          </div>
          <button 
            className={styles.settingsButton}
            onClick={() => {
              navigate(RoutePaths.NOTIFICATION_SETTINGS);
              setIsOpen(false);
            }}
          >
            <FiSettings className={styles.icon} size={16} />
            알림 설정
          </button>
        </div>
      )}
    </div>
  );
};

export default NotificationComponent;