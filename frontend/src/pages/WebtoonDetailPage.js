import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getWebtoonById } from '../services/webtoonService';
import EvaluationSection from '../components/EvaluationSection'; // 평가 섹션 컴포넌트 가져오기
import styles from './WebtoonDetailPage.module.css'; // CSS 모듈 가져오기

const WebtoonDetailPage = () => {
  const { id } = useParams(); // URL에서 웹툰 ID를 가져옴
  const [webtoon, setWebtoon] = useState(null); // 웹툰 데이터를 저장할 상태

  // 웹툰 데이터를 가져오는 비동기 함수
  useEffect(() => {
    const fetchWebtoon = async () => {
      try {
        const response = await getWebtoonById(id); // ID로 웹툰 정보 가져오기

        if (response.success) {
          setWebtoon(response.data); // 가져온 데이터를 상태에 저장
        } else {
          console.error('Failed to fetch webtoon data');
        }
      } catch (error) {
        console.error('Error fetching webtoon data:', error);
      }
    };

    fetchWebtoon();
  }, [id]);

  // 로딩 중이거나 웹툰 데이터가 없을 때 처리
  if (!webtoon) return <div>Loading...</div>;

  return (
    <div className={styles['webtoon-detail-page']}>
      {/* 상단: 웹툰 기본 정보 */}
      <div className={styles['webtoon-info']}>
        <div className={styles['webtoon-image']}>
          <img src={webtoon.thumbnailUrl || 'https://via.placeholder.com/460x623'} alt={webtoon.title} />
        </div>
        <div className={styles['webtoon-details']}>
          <div className={styles['buttons']}>
            <button className={styles['bookmark-button']}>북마크</button>
            <button className={styles['heart-button']}>❤</button>
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

      {/* 웹툰 분석 섹션 (추후 추가 가능) */}
      <div className={styles['webtoon-analysis']}>
        <h2>웹툰 분석</h2>
        {/* 분석 내용 추가 */}
      </div>

      {/* 비슷한 웹툰 추천 섹션 */}
      <div className={styles['similar-webtoons']}>
        <h3>비슷한 웹툰</h3>
        <div className={styles['similar-webtoons-list']}>
          {webtoon.similarWebtoons && webtoon.similarWebtoons.length > 0 ? (
            webtoon.similarWebtoons.map((similar) => (
              <div key={similar.id} className={styles['similar-webtoon-item']}>{similar.title}</div>
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
