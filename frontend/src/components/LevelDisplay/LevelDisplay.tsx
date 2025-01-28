import React from 'react';
import styles from './LevelDisplay.module.css';

interface LevelDisplayProps {
  currentLevel: number; 
  currentPoints: number; 
  maxPoints: number; 
}

const LevelDisplay: React.FC<LevelDisplayProps> = ({ currentLevel, currentPoints, maxPoints }) => {
  const percentage = (currentPoints / maxPoints) * 100; // 경험치 비율 계산

  return (
    <div className={styles.levelContainer}>
      <div className={styles.progressBar}>
        <div className={styles.progress} style={{ width: `${percentage}%` }} />
      </div>
      <div className={styles.levelInfo}>
        <span className={styles.levelValue}> {currentLevel || 0} Lv</span>
      </div>
    </div>
  );
};

export default LevelDisplay; 