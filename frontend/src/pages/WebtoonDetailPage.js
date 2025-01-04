import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getWebtoonById } from '../services/webtoonService';
import { addFavoriteWebtoon, removeFavoriteWebtoon, isFavoriteWebtoon } from '../services/UserService';
import EvaluationSection from '../components/EvaluationSection';
import FavoriteButton from '../components/FavoriteButton';
import BookmarkButton from '../components/BookmarkButton';
import WebtoonAnalysis from '../components/WebtoonAnalysis';
import SimilarWebtoons from '../components/SimilarWebtoons';
import styles from './WebtoonDetailPage.module.css';
import StatusBadge from '../components/StatusBadge';
import PlatformIcon from '../components/PlatformIcon';
import WebtoonTag from '../components/WebtoonTag';

// 웹툰 상세 페이지
const WebtoonDetailPage = () => {
  const { id } = useParams();
  const [webtoon, setWebtoon] = useState(null);
  const [isFavorite, setIsFavorite] = useState(false);
  const [isBookmarked, setIsBookmarked] = useState(false);
  const [isDescriptionExpanded, setIsDescriptionExpanded] = useState(false);
  const [isTagsExpanded, setIsTagsExpanded] = useState(false);

  // 웹툰 데이터 가져오기
  useEffect(() => {
    const fetchWebtoon = async () => {
      try {
        const response = await getWebtoonById(id);
        console.log('API Response:', response);
        
        if (response.success && response.data) {
          // 응답 데이터의 기본값 설정
          const webtoonData = {
            ...response.data,
            platforms: response.data.platforms || [], // platforms가 없으면 빈 배열
            authors: response.data.authors || [],     // authors가 없으면 빈 배열
            tags: response.data.tags || [],          // tags가 없으면 빈 배열
            description: response.data.description || '',  // description이 없으면 빈 문자열
            status: response.data.status || 'UNKNOWN',    // status가 없으면 'UNKNOWN'
            publishDay: response.data.publishDay || '',   // publishDay가 없으면 빈 문자열
            isAdult: response.data.isAdult || false      // isAdult가 없으면 false
          };

          console.log('Processed webtoon data:', webtoonData);
          setWebtoon(webtoonData);
          
          const favoriteStatus = await isFavoriteWebtoon(id);
          setIsFavorite(favoriteStatus);
        } else {
          console.error('Invalid response format:', response);
        }
      } catch (error) {
        console.error('Error fetching webtoon data:', error);
      }
    };

    fetchWebtoon();
  }, [id]);

  // 데이터 로딩 중이면 로딩 표시
  if (!webtoon) {
    return <div>로딩중...</div>;
  }

  // 데이터 구조 검증 및 기본값 설정
  const {
    thumbnailUrl = '',
    title = '',
    isAdult = false,
    status = '',
    publishDay = '',
    platforms = [],
    authors = [],
    description = '',
    tags = []
  } = webtoon;

  // platforms 데이터 구조 확인
  console.log('Platforms in render:', platforms);

  // 좋아요 버튼 클릭 시 처리
  const handleFavoriteClick = async () => {
    try {
      if (isFavorite) {
        await removeFavoriteWebtoon(id);
      } else {
        await addFavoriteWebtoon(id);
      }
      setIsFavorite(!isFavorite);
    } catch (error) {
      console.error('Failed to update favorite status:', error);
    }
  };

  // 북마크 버튼 클릭 핸들러
  const handleBookmarkClick = async () => {
    try {
      // TODO: 북마크 관련 API 호출 로직 구현
      setIsBookmarked(!isBookmarked);
    } catch (error) {
      console.error('Failed to update bookmark status:', error);
    }
  };

  return (
    <div className={styles.webtoonDetailPage}>
      <div className={styles.webtoonInfo}>
        <div className={styles.webtoonImage}>
          <img src={thumbnailUrl || 'https://via.placeholder.com/460x623'} alt={title} />
        </div>
        
        <div className={styles.webtoonDetails}>
          {/* 첫 번째 줄: 상태 뱃지와 플랫폼 아이콘 */}
          <div className={styles.topRow}>
            <div className={styles.statusBadges}>
              {isAdult && <StatusBadge text="19" />}
              {status === 'ONGOING' && <StatusBadge text="연재" />}
              {publishDay && <StatusBadge text={publishDay} />}
            </div>
            <div className={styles.platformIcons}>
              {platforms.map((platform, index) => (
                <PlatformIcon 
                  key={`platform-${index}`} 
                  platform={{
                    id: platform?.id || `platform-${index}`,
                    name: platform?.name || '',
                    iconUrl: platform?.iconUrl || ''
                  }}
                />
              ))}
            </div>
          </div>

          {/* 두 번째 줄: 제목 */}
          <h1 className={styles.title}>{title}</h1>

          {/* 세 번째 줄: 작가 정보 */}
          <div className={styles.authors}>
            {authors.map((author, index) => (
              <span key={author.id || index}>
                {author.name}
                {index < authors.length - 1 && ' · '}
              </span>
            ))}
          </div>

          {/* 네 번째 줄: 시놉시스 */}
          <div className={styles.synopsis}>
            <p className={`${styles.description} ${isDescriptionExpanded ? styles.expanded : ''}`}>
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

          {/* 다섯 번째 줄: 태그 */}
          <div className={styles.tags}>
            <div className={`${styles.tagContainer} ${isTagsExpanded ? styles.expanded : ''}`}>
              {tags.map((tag, index) => (
                <WebtoonTag key={tag.id || index} text={tag.name} />
              ))}
            </div>
            {tags.length > 6 && (
              <button 
                className={styles.expandButton}
                onClick={() => setIsTagsExpanded(!isTagsExpanded)}
              >
                {isTagsExpanded ? '접기' : '더보기'}
              </button>
            )}
          </div>

          {/* 하단 버튼 */}
          <div className={styles.actionButtons}>
            <BookmarkButton 
              isBookmarked={isBookmarked}
              onClick={handleBookmarkClick}
            />
            <FavoriteButton 
              isFavorite={isFavorite} 
              onClick={handleFavoriteClick}
            />
          </div>
        </div>
      </div>

      {/* 평가 섹션 */}
      <EvaluationSection webtoonId={webtoon.id} />

      {/* 웹툰 분석 섹션 */}
      <WebtoonAnalysis analysisData={webtoon.analysisData} />

      {/* 비슷한 웹툰 */}
      <SimilarWebtoons similarWebtoons={webtoon.similarWebtoons} />
    </div>
  );
};

export default WebtoonDetailPage;
