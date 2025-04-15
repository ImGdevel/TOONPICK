import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Webtoon, Platform, SerializationStatus } from '@models/webtoon';
import { FiMoreVertical, FiBookmark, FiAlertCircle, FiShare2, FiPlus, FiEyeOff, FiChevronDown } from 'react-icons/fi';
// import { useAuth } from '@contexts/auth-context';
import StatusTags from './components/status-tags';
import styles from './style.module.css';

interface WebtoonBasicInfoSectionProps {
  webtoon: Webtoon;
}

const WebtoonBasicInfoSection: React.FC<WebtoonBasicInfoSectionProps> = ({ webtoon }) => {
  const navigate = useNavigate();
  // const { isLoggedIn, memberProfile } = useAuth();
  const [isDescriptionExpanded, setIsDescriptionExpanded] = useState(false);
  const [isGenresExpanded, setIsGenresExpanded] = useState(false);
  const [isMoreMenuOpen, setIsMoreMenuOpen] = useState(false);
  const [isBookmarked, setIsBookmarked] = useState(false);

  // 더미 데이터 (실제로는 API에서 받아올 데이터)
  const readingStatus = false ? 'READING' : null; // 'READING', 'COMPLETED', 'DROPPED', null
  const readingProgress = false ? 75 : 0; // 0-100 사이의 값

  const handleReadClick = () => {
    navigate(`/webtoons/${webtoon.id}/read`);
  };

  const handleBookmarkClick = () => {
    setIsBookmarked(!isBookmarked);
  };

  const toggleMoreMenu = () => {
    setIsMoreMenuOpen(!isMoreMenuOpen);
  };

  const toggleDescription = () => {
    setIsDescriptionExpanded(!isDescriptionExpanded);
  };

  const toggleGenres = () => {
    setIsGenresExpanded(!isGenresExpanded);
  };

  const getStatusText = (status: SerializationStatus) => {
    switch (status) {
      case SerializationStatus.ONGOING:
        return '연재중';
      case SerializationStatus.COMPLETED:
        return '완결';
      case SerializationStatus.HIATUS:
        return '휴재';
      default:
        return '';
    }
  };

  const getAuthorText = () => {
    const authorsByRole = webtoon.authors.reduce((acc, author) => {
      if (!acc[author.role]) {
        acc[author.role] = [];
      }
      acc[author.role].push(author.name);
      return acc;
    }, {} as Record<string, string[]>);

    return Object.entries(authorsByRole)
      .map(([role, names]) => `${role} : ${names.join(', ')}`)
      .join(' | ');
  };

  return (
    <section className={styles.section}>
      <div className={styles.contentContainer}>
        <div className={styles.leftSection}>
          <div className={styles.thumbnailContainer}>
            <img 
              src={webtoon.thumbnailUrl} 
              alt={webtoon.title} 
              className={styles.thumbnail}
            />
          </div>
        </div>
        
        <div className={styles.rightSection}>
          <div className={styles.topRow}>
            <StatusTags status={webtoon.status} isAdult={webtoon.isAdult} />
            <div className={styles.actionButtons}>
              <button 
                className={`${styles.bookmarkButton} ${isBookmarked ? styles.bookmarked : ''}`}
                onClick={handleBookmarkClick}
              >
                <FiBookmark />
                {isBookmarked ? '관심' : '관심'}
              </button>
              <div className={styles.moreButtonContainer}>
                <button 
                  className={styles.moreButton}
                  onClick={toggleMoreMenu}
                >
                  <FiMoreVertical />
                </button>
                {isMoreMenuOpen && (
                  <div className={styles.moreMenu}>
                    <button className={styles.menuItem}>
                      <FiShare2 />
                      작품 공유
                    </button>
                    <button className={styles.menuItem}>
                      <FiPlus />
                      컬렉션에 추가
                    </button>
                    <button className={styles.menuItem}>
                      <FiEyeOff />
                      더이상 보지 않기
                    </button>
                    <button className={styles.menuItem}>
                      <FiAlertCircle />
                      정보 오류 신고하기
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>

          <h1 className={styles.title}>{webtoon.title}</h1>

          <div className={styles.authors}>
            {getAuthorText()}
          </div>

          <div className={styles.genresContainer}>
            <div className={`${styles.genreTags} ${isGenresExpanded ? styles.expanded : ''}`}>
              {webtoon.genres.map(genre => (
                <span key={genre.id} className={styles.genreTag}>
                  {genre.name}
                </span>
              ))}
            </div>
            {webtoon.genres.length > 4 && (
              <button 
                className={styles.expandButton}
                onClick={toggleGenres}
              >
                <FiChevronDown />
              </button>
            )}
          </div>

          <div className={styles.descriptionContainer}>
            <p className={`${styles.descriptionText} ${isDescriptionExpanded ? styles.expanded : ''}`}>
              {webtoon.description || ''}
            </p>
            <button 
              className={styles.expandButton}
              onClick={toggleDescription}
            >
              <FiChevronDown />
            </button>
          </div>

          <button 
            className={styles.readButton}
            onClick={handleReadClick}
          >
            첫화보기
          </button>
        </div>
      </div>
    </section>
  );
};

export default WebtoonBasicInfoSection; 