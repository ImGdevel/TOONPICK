import React from 'react';
import styles from './style.module.css';
import { FaTrophy } from 'react-icons/fa';

interface AchievementItemProps {
  title: string;
  count: number;
}

const AchievementItem: React.FC<AchievementItemProps> = ({ title, count }) => {
  return (
    <div className={styles.achievementItem}>
      <div className={styles.icon}>
        <FaTrophy color="#FFD700" />
      </div>
      <div className={styles.details}>
        <span className={styles.title}>{title}</span>
        <span className={styles.count}>{count}</span>
      </div>
    </div>
  );
};

export default AchievementItem; 