// src/components/WebtoonDetailPage.js
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getWebtoonById } from '../services/webtoonService';
import { addFavoriteWebtoon, removeFavoriteWebtoon, isFavoriteWebtoon } from '../services/UserService';
import EvaluationSection from '../components/EvaluationSection';
import styles from './WebtoonDetailPage.module.css';

const WebtoonDetailPage = () => {
  const { id } = useParams(); 
  const [webtoon, setWebtoon] = useState(null);
  const [isFavorite, setIsFavorite] = useState(false);

  useEffect(() => {
    const fetchWebtoon = async () => {
      try {
        const response = await getWebtoonById(id);
        if (response.success) {
          setWebtoon(response.data);
          setIsFavorite(response.data.isFavorite); 
          
          const favoriteStatus = await isFavoriteWebtoon(id);
          setIsFavorite(favoriteStatus);
        } else {
          console.error('Failed to fetch webtoon data');
        }
      } catch (error) {
        console.error('Error fetching webtoon data:', error);
      }
    };
  
    fetchWebtoon();
  }, [id]);

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

  if (!webtoon) return <div>Loading...</div>;

  return (
    <div className={styles['webtoon-detail-page']}>
      <div className={styles['webtoon-info']}>
        <div className={styles['webtoon-image']}>
          <img src={webtoon.thumbnailUrl || 'https://via.placeholder.com/460x623'} alt={webtoon.title} />
        </div>
        <div className={styles['webtoon-details']}>
          <div className={styles['buttons']}>
            <button className={styles['bookmark-button']}>북마크</button>
            <button 
              className={styles['heart-button']} 
              onClick={handleFavoriteClick}>
              {isFavorite ? '❤' : '♡'}
            </button>
          </div>
          <div className={styles['webtoon-title']}>
            {webtoon.title}
          </div>
          <div className={styles['webtoon-meta']}>
            {webtoon.authors.map((author) => author.name).join(', ')} | ★{webtoon.averageRating?.toFixed(1) || 'N/A'}
          </div>
          <div className={styles['extra-info']}>
            {webtoon.description}
          </div>
        </div>
      </div>

      {/* 평가 섹션 */}
      <EvaluationSection webtoonId={webtoon.id} />

      {/* 웹툰 분석 섹션 */}
      <div className={styles['webtoon-analysis']}>
        <h2>웹툰 분석</h2>
        {webtoon.analysisData ? (
          <div className={styles['analysis-content']}>
            <p>조회수: {webtoon.analysisData.views}</p>
            <p>좋아요 수: {webtoon.analysisData.likes}</p>
            <p>댓글 수: {webtoon.analysisData.comments}</p>
          </div>
        ) : (
          <p>분석 데이터가 없습니다.</p>
        )}
      </div>

      {/* 비슷한 웹툰 */}
      <div className={styles['similar-webtoons']}>
        <h3>비슷한 웹툰</h3>
        <div className={styles['similar-webtoons-list']}>
          {webtoon.similarWebtoons && webtoon.similarWebtoons.length > 0 ? (
            webtoon.similarWebtoons.map((similar) => (
              <div key={similar.id} className={styles['similar-webtoon-item']}>
                {similar.title}
              </div>
            ))
          ) : (
            <div>비슷한 웹툰이 없습니다.</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default WebtoonDetailPage;
