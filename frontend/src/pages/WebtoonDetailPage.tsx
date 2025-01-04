import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import styles from './WebtoonDetailPage.module.css';
import { Webtoon } from '../types/webtoon';
import StatusBadge from '../components/StatusBadge';
import PlatformIcon from '../components/PlatformIcon/index';
import { getWebtoonById, isFavoriteWebtoon as checkIsFavorite } from '../services/webtoonService';

const WebtoonDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [webtoon, setWebtoon] = useState<Webtoon | null>(null);
  const [isFavorite, setIsFavorite] = useState<boolean>(false);
  const [isBookmarked, setIsBookmarked] = useState<boolean>(false);
  const [isDescriptionExpanded, setIsDescriptionExpanded] = useState<boolean>(false);
  const [isTagsExpanded, setIsTagsExpanded] = useState<boolean>(false);

  useEffect(() => {
    const fetchWebtoon = async () => {
      try {
        if (!id) return;
        
        const response = await getWebtoonById(id);
        console.log('API Response:', response);
        
        if (response.success && response.data) {
          const webtoonData: Webtoon = {
            ...response.data,
            platforms: response.data.platforms || [],
            authors: response.data.authors || [],
            tags: response.data.tags || [],
            description: response.data.description || '',
            status: response.data.status || 'HIATUS',
            publishDay: response.data.publishDay || '',
            isAdult: response.data.isAdult || false
          };

          console.log('Processed webtoon data:', webtoonData);
          setWebtoon(webtoonData);
          
          const favoriteStatus = await checkIsFavorite(id);
          setIsFavorite(favoriteStatus);
        }
      } catch (error) {
        console.error('Error fetching webtoon data:', error);
      }
    };

    fetchWebtoon();
  }, [id]);

  if (!webtoon) {
    return <div>로딩중...</div>;
  }

  return (
    <div className={styles.webtoonDetailPage}>
      <div className={styles.webtoonInfo}>
        <div className={styles.webtoonImage}>
          <img 
            src={webtoon.thumbnailUrl || 'https://via.placeholder.com/460x623'} 
            alt={webtoon.title} 
          />
        </div>
        
        <div className={styles.webtoonDetails}>
          <div className={styles.topRow}>
            <div className={styles.statusBadges}>
              {webtoon.isAdult && <StatusBadge text="19" />}
              {webtoon.status === 'ONGOING' && <StatusBadge text="연재" />}
              {webtoon.publishDay && <StatusBadge text={webtoon.publishDay} />}
            </div>
            <div className={styles.platformIcons}>
              {webtoon.platforms.map((platform, index) => (
                <PlatformIcon 
                  key={`platform-${index}`} 
                  platform={platform}
                />
              ))}
            </div>
          </div>

          <h1 className={styles.title}>{webtoon.title}</h1>

          <div className={styles.authors}>
            {webtoon.authors.map((author, index) => (
              <span key={`author-${index}`}>
                {author.name}
                {index < webtoon.authors.length - 1 && ' · '}
              </span>
            ))}
          </div>

          {/* ... 나머지 JSX는 동일 ... */}
        </div>
      </div>
    </div>
  );
};

export default WebtoonDetailPage; 