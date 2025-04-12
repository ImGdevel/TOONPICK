import React from 'react';
import { RadarChart, Radar, PolarGrid, PolarAngleAxis, PolarRadiusAxis } from 'recharts';
import styles from './style.module.css';

interface PreferenceCardProps {
  genrePreferences: { name: string; value: number }[];
  emotionalTags: string[];
  aiTags: string[];
}

const PreferenceCard: React.FC<PreferenceCardProps> = ({
  genrePreferences,
  emotionalTags,
  aiTags,
}) => {
  return (
    <div className={styles.preferenceCard}>
      <h2 className={styles.cardTitle}>나의 웹툰 취향</h2>
      <div className={styles.chartContainer}>
        <RadarChart outerRadius={90} width={300} height={300} data={genrePreferences}>
          <PolarGrid />
          <PolarAngleAxis dataKey="name" />
          <PolarRadiusAxis />
          <Radar
            name="Genre Preference"
            dataKey="value"
            stroke="#8884d8"
            fill="#8884d8"
            fillOpacity={0.6}
          />
        </RadarChart>
      </div>
      <div className={styles.tagsContainer}>
        <div className={styles.tagSection}>
          <h3>감정 태그</h3>
          <div className={styles.tagList}>
            {emotionalTags.map((tag) => (
              <span key={tag} className={styles.emotionTag}>
                {tag}
              </span>
            ))}
          </div>
        </div>
        <div className={styles.tagSection}>
          <h3>AI 추천 태그</h3>
          <div className={styles.tagList}>
            {aiTags.map((tag) => (
              <span key={tag} className={styles.aiTag}>
                #{tag}
              </span>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default PreferenceCard;
