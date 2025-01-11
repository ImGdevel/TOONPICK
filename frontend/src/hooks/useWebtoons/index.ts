import { useState, useEffect } from 'react';
import { Webtoon } from '../../models/webtoon';
import WebtoonService from '../../services/webtoonService';
import { SerializationStatus } from '@/models/enum';

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
          WebtoonService.getWebtoons({
            page: 0,
            serializationStatus: SerializationStatus.ONGOING,
          }),
          WebtoonService.getWebtoons({
            page: 0,
            serializationStatus: SerializationStatus.COMPLETED,
          })
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