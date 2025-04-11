import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Webtoon, Platform, SerializationStatus } from '@models/webtoon';
// import { useAuth } from '@contexts/auth-context';
import styles from './style.module.css';

interface WebtoonBasicInfoSectionProps {
  webtoon: Webtoon;
}

const WebtoonBasicInfoSection: React.FC<WebtoonBasicInfoSectionProps> = ({ webtoon }) => {
  const navigate = useNavigate();
  // const { isLoggedIn, memberProfile } = useAuth();
  const [isDescriptionExpanded, setIsDescriptionExpanded] = useState(false);

  // 더미 데이터 (실제로는 API에서 받아올 데이터)
  const readingStatus = false ? 'READING' : null; // 'READING', 'COMPLETED', 'DROPPED', null
  const readingProgress = false ? 75 : 0; // 0-100 사이의 값
  const isBookmarked = false ? true : false;

  const handleReadClick = () => {
    // 읽기 페이지로 이동 (아직 구현되지 않음)
    navigate(`/webtoons/${webtoon.id}/read`);
  };

  const handleBookmarkClick = () => {
    // 북마크 추가/제거 (아직 구현되지 않음)
    console.log('북마크 토글');
  };

  const toggleDescription = () => {
    setIsDescriptionExpanded(!isDescriptionExpanded);
  };

  // 설명 텍스트가 200자 이상인 경우 자르기
  const descriptionText = webtoon.description || '설명이 없습니다.';
  const shouldTruncate = descriptionText.length > 100;
  const displayText = isDescriptionExpanded || !shouldTruncate 
    ? descriptionText 
    : `${descriptionText.substring(0, 100)}...`;

  return (
    <section className={styles.section}>
      <h2 className={styles.sectionTitle}>웹툰 정보</h2>
      
      <div className={styles.contentContainer}>
        <div className={styles.leftSection}>
          <div className={styles.thumbnailContainer}>
            <img 
              src={webtoon.thumbnailUrl} 
              alt={webtoon.title} 
              className={styles.thumbnail}
            />
            
            {/* 상태 뱃지 */}
            <div className={styles.badgeContainer}>
              {webtoon.isAdult && <span className={styles.adultBadge}>성인</span>}
              {webtoon.status === SerializationStatus.COMPLETED && (
                <span className={styles.completedBadge}>완결</span>
              )}
              {webtoon.status === SerializationStatus.HIATUS && (
                <span className={styles.hiatusBadge}>휴재</span>
              )}
            </div>
          </div>
          
          <div className={styles.actionButtons}>
            <button 
              className={styles.readButton}
              onClick={handleReadClick}
            >
              {readingStatus === 'READING' ? '이어보기' : '읽기 시작'}
            </button>
            
            <button 
              className={`${styles.bookmarkButton} ${isBookmarked ? styles.bookmarked : ''}`}
              onClick={handleBookmarkClick}
            >
              {isBookmarked ? '북마크됨' : '북마크'}
            </button>
          </div>
        </div>
        
        <div className={styles.rightSection}>
          <h1 className={styles.title}>{webtoon.title}</h1>
          
          <div className={styles.platform}>
            <span className={styles.label}>연재 플랫폼:</span>
            <span className={styles.value}>{webtoon.platform}</span>
          </div>
          
          <div className={styles.authors}>
            <span className={styles.label}>작가:</span>
            <span className={styles.value}>
              {webtoon.authors ? webtoon.authors.map(author => author.name).join(', ') : '정보 없음'}
            </span>
          </div>
          
          <div className={styles.publishDay}>
            <span className={styles.label}>연재일:</span>
            <span className={styles.value}>{webtoon.publishDay}</span>
          </div>
          
          <div className={styles.genres}>
            <span className={styles.label}>장르:</span>
            <div className={styles.genreTags}>
              {webtoon.genres ? webtoon.genres.map(genre => (
                <span key={genre.id} className={styles.genreTag}>
                  {genre.name}
                </span>
              )) : '정보 없음'}
            </div>
          </div>
          
          <div className={styles.description}>
            <span className={styles.label}>줄거리:</span>
            <p className={styles.descriptionText}>
              {displayText}
              {shouldTruncate && (
                <button 
                  className={styles.expandButton}
                  onClick={toggleDescription}
                >
                  {isDescriptionExpanded ? '접기' : '더보기'}
                </button>
              )}
            </p>
          </div>
          
          {false && readingStatus && (
            <div className={styles.readingProgress}>
              <span className={styles.label}>읽기 진행률:</span>
              <div className={styles.progressBar}>
                <div 
                  className={styles.progressFill} 
                  style={{ width: `${readingProgress}%` }}
                />
              </div>
              <span className={styles.progressText}>{readingProgress}%</span>
            </div>
          )}
        </div>
      </div>
    </section>
  );
};

export default WebtoonBasicInfoSection; 