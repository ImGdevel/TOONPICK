import React, { useState, useEffect } from 'react';
import { Webtoon } from '@models/webtoon';
import StatusBadge from '@components/StatusBadge';
import PlatformIcon from '@components/PlatformIcon';
import WebtoonTag from '@components/WebtoonTag/WebtoonTag';
import styles from './WebtoonDetailsSection.module.css';

interface WebtoonDetailsSectionProps {
  webtoon: Webtoon;
}

const DEFAULT_IMAGE_URL = 'https://via.placeholder.com/460x623';
const MAX_DESCRIPTION_LENGTH = 200;
const MAX_TAGS_VISIBLE = 6;

const WebtoonDetailsSection: React.FC<WebtoonDetailsSectionProps> = ({ webtoon }) => {
  const [isDescriptionExpanded, setIsDescriptionExpanded] = useState<boolean>(false);
  const [isTagsExpanded, setIsTagsExpanded] = useState<boolean>(false);
  const [webtoonData, setWebtoonData] = useState<Webtoon | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (webtoon) {
      setWebtoonData(webtoon);
      setLoading(false);
    } else {
      setError('웹툰 정보를 불러오는 데 실패했습니다.');
      setLoading(false);
    }
  }, [webtoon]);

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;
  if (!webtoonData) return null;

  return (
    <section className={styles.webtoonInfo}>

      {/* 썸네일 */}
      <div className={styles.webtoonImage}>
        <img src={webtoonData.thumbnailUrl || DEFAULT_IMAGE_URL} alt={webtoonData.title} />
      </div>
      
      {/* 웹툰 정보 */}
      <div className={styles.webtoonDetails}>
        <h1 className={styles.webtoonTitle}>{webtoonData.title}</h1>
        

        <div className={styles.webtoonMeta}>
          
          <div className={styles.statusBadges}>
            {webtoonData.isAdult && <StatusBadge text="19" />}
            {webtoonData.status === 'ONGOING' && <StatusBadge text="연재" />}
            {webtoonData.publishDay && <StatusBadge text={webtoonData.publishDay} />}
          </div>

          <div className={styles.platformIcons}>
            {webtoonData.platform ? (
              <PlatformIcon platform={webtoonData.platform} />
            ) : (
              <span></span>
            )}
          </div>
          <div className={styles.ratings}>
            <span>총 평점: {webtoonData.totalRatings}</span>
            <span>평균 평점: {webtoonData.averageRating}</span>
          </div>
        </div>

        {/* 작가 */}
        <div className={styles.authors}>
          {webtoonData.authors.map((author, index) => (
            <span key={author.id}>
              {author.name}
              {index < webtoonData.authors.length - 1 && ' · '}
            </span>
          ))}
        </div>

        {/* 줄거리 */}
        <div className={styles.synopsis}>
          <p className={`${styles.description} ${isDescriptionExpanded ? styles.expanded : ''}`}>
            {webtoonData.description}
          </p>
          {webtoonData.description.length > MAX_DESCRIPTION_LENGTH && (
            <button 
              className={styles.expandButton}
              onClick={() => setIsDescriptionExpanded(!isDescriptionExpanded)}
            >
              {isDescriptionExpanded ? '접기' : '더보기'}
            </button>
          )}
        </div>

        {/* 장르 */}
        <div className={styles.tags}>
          <div className={`${styles.tagContainer} ${isTagsExpanded ? styles.expanded : ''}`}>
            {webtoonData.genres && webtoonData.genres.length > 0 ? (
              webtoonData.genres.map((tag) => (
                <WebtoonTag key={tag.id} text={tag.name} />
              ))
            ) : (
              <span>장르 정보가 없습니다.</span>
            )}
          </div>
          {webtoonData.genres && webtoonData.genres.length > MAX_TAGS_VISIBLE && (
            <button 
              className={styles.expandButton}
              onClick={() => setIsTagsExpanded(!isTagsExpanded)}
              aria-label={isTagsExpanded ? '장르 접기' : '장르 더보기'}
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