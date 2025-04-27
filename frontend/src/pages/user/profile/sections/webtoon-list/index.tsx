import React from 'react';
import { Webtoon } from '@models/webtoon';
import WebtoonList from '@components/webtoon-list';
import styles from './style.module.css';

interface WebtoonListSectionProps {
  title: string;
  webtoons: Webtoon[];
  showMoreLink?: string;
}

const WebtoonListSection: React.FC<WebtoonListSectionProps> = ({
  title,
  webtoons,
  showMoreLink,
}) => {
  return (
    <section className={styles.webtoonListSection}>
      <div className={styles.sectionHeader}>
        <h2 className={styles.sectionTitle}>{title}</h2>
        {showMoreLink && (
          <a href={showMoreLink} className={styles.showMoreLink}>
            더보기
          </a>
        )}
      </div>
      <WebtoonList webtoons={webtoons} />
    </section>
  );
};

export default WebtoonListSection;
