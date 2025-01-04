import React from 'react';
import { Webtoon } from '@/types/webtoon';
import WebtoonItem from '@/components/WebtoonItem';
import styles from './WebtoonList.module.css';

interface WebtoonListProps {
  webtoons: Webtoon[];
  title?: string;
  showPublisher?: boolean;
  emptyMessage?: string;
}

const WebtoonList: React.FC<WebtoonListProps> = ({
  webtoons,
  title,
  showPublisher = true,
  emptyMessage = "웹툰이 없습니다."
}) => {
  return (
    <div className={styles.webtoonList}>
      {title && <h2 className={styles.title}>{title}</h2>}
      {webtoons.length > 0 ? (
        <div className={styles.list}>
          {webtoons.map((webtoon) => (
            <WebtoonItem
              key={webtoon.id}
              webtoon={webtoon}
              showPublisher={showPublisher}
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