import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Webtoon } from '@models/webtoon';
import WebtoonService from '@services/webtoon-service';
import WebtoonBasicInfoSection from './sections/webtoon-basic-info-section';
import WebtoonRatingSection from './sections/webtoon-rating-section';
// import WebtoonAnalysisSection from './sections/webtoon-analysis-section';
// import WebtoonRecommendationSection from './sections/webtoon-recommendation-section';
import styles from './style.module.css';

const WebtoonDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [webtoon, setWebtoon] = useState<Webtoon | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchWebtoon = async () => {
      try {
        setLoading(true);
        if (!id) return;

        const response = await WebtoonService.getWebtoonById(parseInt(id));
        if (response.success && response.data) {
          setWebtoon(response.data);
        } else {
          setError('웹툰 정보를 불러오는데 실패했습니다.');
        }
      } catch (error) {
        console.error('Failed to fetch webtoon:', error);
        setError('웹툰 정보를 불러오는데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchWebtoon();
  }, [id]);

  if (loading) {
    return <div className={styles.loading}>로딩 중...</div>;
  }

  if (error) {
    return <div className={styles.error}>{error}</div>;
  }

  if (!webtoon) {
    return <div className={styles.error}>웹툰을 찾을 수 없습니다.</div>;
  }

  return (
    <div className={styles.container}>
      <WebtoonBasicInfoSection webtoon={webtoon} />
      <WebtoonRatingSection webtoon={webtoon} />
      {/* <WebtoonAnalysisSection webtoon={webtoon} />
      <WebtoonRecommendationSection webtoon={webtoon} /> */}
    </div>
  );
};

export default WebtoonDetailPage;