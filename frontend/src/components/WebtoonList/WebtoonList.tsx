import React from 'react';
import { Webtoon } from '@models/webtoon';
import WebtoonCard from '@components/WebtoonCard';
import styles from './WebtoonList.module.css';

interface WebtoonListProps {
  webtoons: Webtoon[];
  title?: string;
  showTags?: boolean;
  emptyMessage?: string;
}

const WebtoonList: React.FC<WebtoonListProps> = ({
  webtoons,
  title,
  showTags = true,
  emptyMessage = "웹툰이 없습니다."
}) => {
  return (
    <div className={styles.webtoonList}>
      {title && <h2 className={styles.title}>{title}</h2>}
      {webtoons.length > 0 ? (
        <div className={styles.list}>
          {webtoons.map((webtoon) => (
            <WebtoonCard
              key={webtoon.id}
              webtoon={webtoon}
              showTags={showTags}
            />
          ))}
        </div>
      ) : (
        <p className={styles.emptyMessage}>{emptyMessage}</p>
      )}
    </div>
  );
};

export default WebtoonList; 