import React from 'react';
import { Webtoon } from '@models/webtoon';
import styles from './style.module.css';

interface ReadingHistorySectionProps {
  readingHistory: {
    webtoon: Webtoon;
    lastReadAt: string;
  }[];
}

const ReadingHistorySection: React.FC<ReadingHistorySectionProps> = ({
  readingHistory,
}) => {
  return (
    <section className={styles.readingHistorySection}>
      <h2 className={styles.sectionTitle}>웹툰 감상 히스토리</h2>
      <div className={styles.calendarView}>
        {/* 캘린더 뷰 구현 */}
      </div>
      <div className={styles.recentHistory}>
        <h3>최근 감상한 웹툰</h3>
        <div className={styles.historyList}>
          {readingHistory.slice(0, 5).map((history) => (
            <div key={history.webtoon.id} className={styles.historyItem}>
              <img
                src={history.webtoon.thumbnailUrl}
                alt={history.webtoon.title}
                className={styles.thumbnail}
              />
              <div className={styles.historyInfo}>
                <span className={styles.webtoonTitle}>{history.webtoon.title}</span>
                <span className={styles.lastReadAt}>{history.lastReadAt}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default ReadingHistorySection;
