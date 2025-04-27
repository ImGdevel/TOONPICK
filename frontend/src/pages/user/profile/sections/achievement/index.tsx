import React from 'react';
import styles from './style.module.css';

interface AchievementSectionProps {
  badges: {
    id: number;
    name: string;
    icon: string;
  }[];
}

const AchievementSection: React.FC<AchievementSectionProps> = ({
  badges,
}) => {
  return (
    <section className={styles.achievementSection}>
      <h2 className={styles.sectionTitle}>나의 업적</h2>
      <div className={styles.badgeGrid}>
        {badges.map((badge) => (
          <div key={badge.id} className={styles.badgeCard}>
            <img src={badge.icon} alt={badge.name} className={styles.badgeIcon} />
            <span className={styles.badgeName}>{badge.name}</span>
          </div>
        ))}
      </div>
    </section>
  );
};

export default AchievementSection;
