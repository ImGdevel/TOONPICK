import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import WebtoonService from '@services/webtoonService';
import UserService from '@services/UserService';
import EvaluationSection from '@components/EvaluationSection/EvaluationSection';
import FavoriteButton from '@components/FavoriteButton';
import BookmarkButton from '@components/BookMarkButton';
import WebtoonAnalysis from '@components/WebtoonAnalysis/WebtoonAnalysis';
import SimilarWebtoons from '@components/SimilarWebtoons';
import StatusBadge from '@components/StatusBadge';
import PlatformIcon from '@components/PlatformIcon';
import WebtoonTag from '@components/WebtoonTag/WebtoonTag';
import WebtoonReviewService from '@services/WebtoonReviewService';
import { Webtoon, Platform } from '@models/webtoon';
import styles from './WebtoonDetailPage.module.css';


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
        
        const response = await WebtoonService.getWebtoonById(parseInt(id));
        
        if (response?.success && response.data) {
          const webtoonData: Webtoon = {
            ...response.data,
            platforms: response.data.platforms.map((platform: string) => platform as Platform),
            authors: response.data.authors || [],
            tags: response.data.tags || [],
            description: response.data.description || '',
            status: response.data.status || 'UNKNOWN',
            publishDay: response.data.publishDay || '',
            isAdult: response.data.isAdult || false
          };

          setWebtoon(webtoonData);
          
          const favoriteStatus = await UserService.isFavoriteWebtoon(parseInt(id));
          setIsFavorite(favoriteStatus.data || false);
        }
      } catch (error) {
        console.error('Error fetching webtoon data:', error);
      }
    };

    fetchWebtoon();
  }, [id]);

  const handleFavoriteClick = async () => {
    try {
      if (!id) return;
      
      if (isFavorite) {
        await UserService.removeFavoriteWebtoon(parseInt(id));
      } else {
        await UserService.addFavoriteWebtoon(parseInt(id));
      }
      setIsFavorite(!isFavorite);
    } catch (error) {
      console.error('Failed to update favorite status:', error);
    }
  };

  const handleBookmarkClick = async () => {
    try {
      setIsBookmarked(!isBookmarked);
    } catch (error) {
      console.error('Failed to update bookmark status:', error);
    }
  };

  const handleRatingSubmit = async (rating: number, comment: string) => {
    if (!webtoon) return;
    
    try {
      await WebtoonReviewService.createWebtoonReview(webtoon.id, rating, comment);
    } catch (error) {
      console.error('Failed to submit rating:', error);
    }
  };

  if (!webtoon) {
    return <div>로딩중...</div>;
  }

  return (
    <div className={styles.webtoonDetailPage}>
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
              {webtoon.platforms.map((platform) => (
                <PlatformIcon 
                  platform={platform}
                />
              ))}
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
              {webtoon.tags.map((tag) => (
                <WebtoonTag key={tag.id} text={tag.name} />
              ))}
            </div>
            {webtoon.tags.length > 6 && (
              <button 
                className={styles.expandButton}
                onClick={() => setIsTagsExpanded(!isTagsExpanded)}
              >
                {isTagsExpanded ? '접기' : '더보기'}
              </button>
            )}
          </div>

          <div className={styles.actionButtons}>
            <BookmarkButton 
              isBookmarked={isBookmarked}
              onClick={handleBookmarkClick}
            />
            <FavoriteButton 
              isFavorited={isFavorite} 
              onClick={handleFavoriteClick}
            />
          </div>
        </div>
      </section>

      <EvaluationSection 
        webtoonId={webtoon.id} 
        totalRatings={webtoon.totalRatings} 
        averageRating={webtoon.averageRating} 
        onRatingSubmit={handleRatingSubmit} 
      />
      <WebtoonAnalysis analysisData={webtoon.analysisData} />
      <SimilarWebtoons 
        similarWebtoons={webtoon.similarWebtoons || []} 
        currentWebtoonId={webtoon.id} 
      />
    </div>
  );
};

export default WebtoonDetailPage;