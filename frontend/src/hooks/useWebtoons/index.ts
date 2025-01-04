import { useState, useEffect } from 'react';
import { Webtoon } from '../../types/webtoon';
import WebtoonService from '../../services/webtoonService';

interface WebtoonListResponse {
  data: Webtoon[];
}

export const useWebtoons = () => {
  const [popularWebtoons, setPopularWebtoons] = useState<Webtoon[]>([]);
  const [recentWebtoons, setRecentWebtoons] = useState<Webtoon[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchWebtoons = async () => {
      try {
        const [popular, recent] = await Promise.all([
          WebtoonService.getPopularWebtoons(),
          WebtoonService.getRecentWebtoons()
        ]) as [WebtoonListResponse, WebtoonListResponse];
        
        setPopularWebtoons(popular.data);
        setRecentWebtoons(recent.data);
      } catch (err) {
        setError('웹툰을 불러오는데 실패했습니다.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchWebtoons();
  }, []);

  return { popularWebtoons, recentWebtoons, isLoading, error };
}; 