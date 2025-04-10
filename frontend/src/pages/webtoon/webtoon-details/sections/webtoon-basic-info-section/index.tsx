import React, { useState } from 'react';
import { Webtoon } from '@models/webtoon';
import styles from './style.module.css';

interface WebtoonBasicInfoSectionProps {
  webtoon: Webtoon;
}

const WebtoonBasicInfoSection: React.FC<WebtoonBasicInfoSectionProps> = ({ webtoon }) => {
  const [isDescriptionExpanded, setIsDescriptionExpanded] = useState(false);

  const {
    title,
    thumbnailUrl,
    platform,
    isAdult,
    status,
    publishDay,
    authors,
    description,
    genres
  } = webtoon;

  const handleReadClick = () => {
    // 읽기 페이지로 이동하는 로직 구현 필요
  };

  const handleBookmarkClick = () => {
    // 북마크 추가/제거 로직 구현 필요
  };

  return (
    <section className={styles.basicInfoSection}>
      <div className={styles.thumbnailSection}>
        <img src={thumbnailUrl} alt={title} className={styles.thumbnail} />
        {isAdult && <div className={styles.adultBadge}>19</div>}
        <div className={styles.statusBadge}>{status}</div>
      </div>

      <div className={styles.infoSection}>
        <h1 className={styles.title}>{title}</h1>
        <div className={styles.platform}>{platform}</div>
        <div className={styles.authors}>
          {authors.map((author) => (
            <span key={author.id} className={styles.author}>
              {author.name}
            </span>
          ))}
        </div>
        <div className={styles.publishDay}>연재일: {publishDay}</div>
        <div className={styles.genres}>
          {genres.map((genre) => (
            <span key={genre.id} className={styles.genre}>
              {genre.name}
            </span>
          ))}
        </div>
        <div className={styles.description}>
          <p className={isDescriptionExpanded ? styles.expanded : styles.collapsed}>
            {description}
          </p>
          {description.length > 200 && (
            <button
              className={styles.expandButton}
              onClick={() => setIsDescriptionExpanded(!isDescriptionExpanded)}
            >
              {isDescriptionExpanded ? '접기' : '더보기'}
            </button>
          )}
        </div>
      </div>

      <div className={styles.actionButtons}>
        <button className={styles.readButton} onClick={handleReadClick}>
          읽기
        </button>
        <button className={styles.bookmarkButton} onClick={handleBookmarkClick}>
          북마크
        </button>
      </div>
    </section>
  );
};

export default WebtoonBasicInfoSection; 