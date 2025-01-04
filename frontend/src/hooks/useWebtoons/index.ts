import { useState, useEffect } from 'react';
import { Webtoon } from '../../types/webtoon';
import { getPopularWebtoons, getRecentWebtoons, getRecommendedWebtoons } from '../../services/webtoonService';

interface WebtoonListResponse {
  data: Webtoon[];
}

export const useWebtoons = () => {
  const [popularWebtoons, setPopularWebtoons] = useState<Webtoon[]>([]);
  const [recentWebtoons, setRecentWebtoons] = useState<Webtoon[]>([]);
  const [recommendedWebtoons, setRecommendedWebtoons] = useState<Webtoon[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchWebtoons = async () => {
      try {
        const [popular, recent, recommended] = await Promise.all([
          getPopularWebtoons(),
          getRecentWebtoons(),
          getRecommendedWebtoons()
        ]) as [WebtoonListResponse, WebtoonListResponse, WebtoonListResponse];
        
        setPopularWebtoons(popular.data);
        setRecentWebtoons(recent.data);
        setRecommendedWebtoons(recommended.data);
      } catch (err) {
        setError('웹툰을 불러오는데 실패했습니다.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchWebtoons();
  }, []);

  return { popularWebtoons, recentWebtoons, recommendedWebtoons, isLoading, error };
}; 