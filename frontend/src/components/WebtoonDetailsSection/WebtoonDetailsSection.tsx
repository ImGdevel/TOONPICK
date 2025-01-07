import React, { useState } from 'react';
import { Webtoon } from '@models/webtoon';
import StatusBadge from '@components/StatusBadge';
import PlatformIcon from '@components/PlatformIcon';
import WebtoonTag from '@components/WebtoonTag/WebtoonTag';
import styles from './WebtoonDetailsSection.module.css';

interface WebtoonDetailsSectionProps {
  webtoon: Webtoon;
}

const WebtoonDetailsSection: React.FC<WebtoonDetailsSectionProps> = ({ webtoon }) => {
  const [isDescriptionExpanded, setIsDescriptionExpanded] = useState<boolean>(false);
  const [isTagsExpanded, setIsTagsExpanded] = useState<boolean>(false);

  return (
    <section className={styles.webtoonInfo}>
      <div className={styles.webtoonImage}>
        <img src={webtoon.thumbnailUrl || 'https://via.placeholder.com/460x623'} alt={webtoon.title} />
      </div>
      
      <div className={styles.webtoonDetails}>
        <h1 className={styles.webtoonTitle}>{webtoon.title}</h1>
        <div className={styles.webtoonMeta}>
          <div className={styles.statusBadges}>
            {webtoon.isAdult && <StatusBadge text="19" />}
            {webtoon.status === 'ONGOING' && <StatusBadge text="연재" />}
            {webtoon.publishDay && <StatusBadge text={webtoon.publishDay} />}
          </div>
          <div className={styles.platformIcons}>
            {webtoon.platforms ? (
              <PlatformIcon platform={webtoon.platforms} />
            ) : (
              <span>플랫폼 정보가 없습니다.</span>
            )}
          </div>
        </div>

        <div className={styles.authors}>
          {webtoon.authors.map((author, index) => (
            <span key={author.id}>
              {author.name}
              {index < webtoon.authors.length - 1 && ' · '}
            </span>
          ))}
        </div>

        <div className={styles.synopsis}>
          <p className={`${styles.description} ${isDescriptionExpanded ? styles.expanded : ''}`}>
            {webtoon.description}
          </p>
          {webtoon.description.length > 200 && (
            <button 
              className={styles.expandButton}
              onClick={() => setIsDescriptionExpanded(!isDescriptionExpanded)}
            >
              {isDescriptionExpanded ? '접기' : '더보기'}
            </button>
          )}
        </div>

        <div className={styles.tags}>
          <div className={`${styles.tagContainer} ${isTagsExpanded ? styles.expanded : ''}`}>
            {webtoon.genre && webtoon.genre.length > 0 ? (
              webtoon.genre.map((tag) => (
                <WebtoonTag key={tag.id} text={tag.name} />
              ))
            ) : (
              <span>장르 정보가 없습니다.</span>
            )}
          </div>
          {webtoon.genre && webtoon.genre.length > 6 && (
            <button 
              className={styles.expandButton}
              onClick={() => setIsTagsExpanded(!isTagsExpanded)}
            >
              {isTagsExpanded ? '접기' : '더보기'}
            </button>
          )}
        </div>
      </div>
    </section>
  );
};

export default WebtoonDetailsSection; 