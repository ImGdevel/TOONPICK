import React, { useState, useEffect, useRef } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import styles from './style.module.css';
import WebtoonService from '@services/webtoon-service';
import WebtoonGrid from '@components/webtoon-grid';
import Spinner from '@components/spinner';
import { Webtoon, Platform } from '@models/webtoon';
import { DayOfWeek } from '@models/enum';

const OngoingWebtoonsPage: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();
  
  const [webtoons, setWebtoons] = useState<Webtoon[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [isLastPage, setIsLastPage] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  // 필터 상태
  const [selectedPlatforms, setSelectedPlatforms] = useState<Platform[]>(() => {
    const platforms = searchParams.get('platforms');
    return platforms ? platforms.split(',').map(p => p as Platform) : [];
  });
  
  const [selectedDay, setSelectedDay] = useState<DayOfWeek | null>(() => {
    const day = searchParams.get('day');
    if (day) return day as DayOfWeek;
    
    // 오늘 요일을 기본값으로 설정
    const today = new Date().getDay();
    const dayMap: { [key: number]: DayOfWeek } = {
      0: 'SUNDAY',
      1: 'MONDAY',
      2: 'TUESDAY',
      3: 'WEDNESDAY',
      4: 'THURSDAY',
      5: 'FRIDAY',
      6: 'SATURDAY'
    };
    return dayMap[today];
  });
  
  const [sortBy, setSortBy] = useState<'POPULARITY' | 'RATING' | 'LATEST' | 'UPDATE'>(() => {
    const sort = searchParams.get('sort');
    return (sort as 'POPULARITY' | 'RATING' | 'LATEST' | 'UPDATE') || 'POPULARITY';
  });

  const observer = useRef<IntersectionObserver | null>(null);
  const lastWebtoonRef = useRef<HTMLDivElement | null>(null);

  const DAYS: DayOfWeek[] = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
  const DAY_LABELS: Record<DayOfWeek, string> = {
    'ALL': '전체',
    'MONDAY': '월',
    'TUESDAY': '화',
    'WEDNESDAY': '수',
    'THURSDAY': '목',
    'FRIDAY': '금',
    'SATURDAY': '토',
    'SUNDAY': '일'
  };

  const PLATFORM_LABELS: Record<Platform, string> = {
    [Platform.NAVER]: '네이버',
    [Platform.KAKAO]: '카카오',
    [Platform.KAKAOPAGE]: '카카오페이지',
    [Platform.LEZHIN]: '레진',
    [Platform.BOMTOON]: '봄툰',
  };

  const SORT_OPTIONS = [
    { value: 'POPULARITY', label: '인기순' },
    { value: 'RATING', label: '별점순' },
    { value: 'LATEST', label: '최신순' },
    { value: 'UPDATE', label: '업데이트순' }
  ];

  // URL 파라미터 업데이트 함수
  const updateUrlParams = () => {
    const params = new URLSearchParams();
    if (selectedPlatforms.length > 0) {
      params.set('platforms', selectedPlatforms.join(','));
    }
    if (selectedDay) {
      params.set('day', selectedDay);
    }
    if (sortBy !== 'POPULARITY') {
      params.set('sort', sortBy);
    }
    setSearchParams(params);
  };

  // 필터 상태 변경 시 URL 업데이트
  useEffect(() => {
    updateUrlParams();
  }, [selectedPlatforms, selectedDay, sortBy]);

  const handlePlatformChange = (platform: Platform) => {
    setSelectedPlatforms(prev => 
      prev.includes(platform)
        ? prev.filter(p => p !== platform)
        : [...prev, platform]
    );
  };

  const handleSelectAllPlatforms = () => {
    if (selectedPlatforms.length === Object.values(Platform).length) {
      setSelectedPlatforms([]);
    } else {
      setSelectedPlatforms(Object.values(Platform));
    }
  };

  const handleDayChange = (day: DayOfWeek) => {
    setSelectedDay(day === selectedDay ? null : day);
  };

  const fetchOngoingWebtoons = async (page: number) => {
    setIsLoading(true);
    console.log("데이터 가져오기!")
    try {
      const response = await WebtoonService.getWebtoons({
        page,
        platforms: selectedPlatforms.length > 0 ? selectedPlatforms : undefined,
        publishDays: selectedDay ? [selectedDay] : undefined,
        sortBy
      });
      const { data, last } = response;

      setWebtoons((prev) => page === 0 ? (data || []) : [...prev, ...(data || [])]);
      setIsLastPage(last || false);
    } catch (error) {
      setError('진행 중인 웹툰을 불러오는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  // 필터 변경 시 데이터 리셋 및 재로드
  useEffect(() => {
    setCurrentPage(0);
    setWebtoons([]);
    setIsLastPage(false);
    fetchOngoingWebtoons(0);
  }, [selectedPlatforms, selectedDay, sortBy]);

  // IntersectionObserver 설정
  useEffect(() => {
    const observerCallback = (entries: IntersectionObserverEntry[]) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting && !isLoading && !isLastPage) {
          setCurrentPage((prev) => prev + 1);
        }
      });
    };

    if (!observer.current) {
      observer.current = new IntersectionObserver(observerCallback, {
        threshold: 0.3, // 요소가 10% 보이면 트리거
        rootMargin: '1000px' // 뷰포트 아래 100px에서 미리 로드
      });
    }

    const currentRef = lastWebtoonRef.current;
    if (currentRef) {
      observer.current.observe(currentRef);
    }

    return () => {
      if (currentRef) {
        observer.current?.unobserve(currentRef);
      }
    };
  }, [isLoading, isLastPage]);

  // 새로운 페이지 로드
  useEffect(() => {
    if (currentPage > 0 && !isLastPage) {
      fetchOngoingWebtoons(currentPage);
    }
  }, [currentPage, isLastPage]);

  return (
    <div className={styles.ongoingWebtoonsPage}>

      <div className={styles.filters}>
        <div className={styles.filterGroup}>
          <div className={styles.platformButtons}>
            <button
              className={`${styles.filterButton} ${selectedPlatforms.length === Object.values(Platform).length ? styles.active : ''}`}
              onClick={handleSelectAllPlatforms}
            >
              전체
            </button>
            {Object.values(Platform).map((platform) => (
              <button
                key={platform}
                className={`${styles.filterButton} ${selectedPlatforms.includes(platform) ? styles.active : ''}`}
                onClick={() => handlePlatformChange(platform)}
              >
                {PLATFORM_LABELS[platform]}
              </button>
            ))}
          </div>
        </div>

        <div className={styles.filterGroup}>
          <div className={styles.dayButtons}>
            {DAYS.map((day) => (
              <button
                key={day}
                className={`${styles.filterButton} ${selectedDay === day ? styles.active : ''}`}
                onClick={() => handleDayChange(day)}
              >
                {DAY_LABELS[day]}
              </button>
            ))}
          </div>
        </div>

        <div className={styles.sortOptions}>
          {SORT_OPTIONS.map((option) => (
            <button
              key={option.value}
              className={`${styles.sortButton} ${sortBy === option.value ? styles.active : ''}`}
              onClick={() => setSortBy(option.value as typeof sortBy)}
            >
              {option.label}
            </button>
          ))}
        </div>
      </div>

      {error ? (
        <div className={styles.error}>
          <p>{error}</p>
          <button onClick={() => fetchOngoingWebtoons(currentPage)}>재시도</button>
        </div>
      ) : (
        <>
          <WebtoonGrid
            webtoons={webtoons}
            lastWebtoonRef={lastWebtoonRef}
          />
          {isLoading && <Spinner />}
          {isLastPage && <p className={styles.endMessage}>모든 웹툰을 불러왔습니다.</p>}
        </>
      )}
    </div>
  );
};

export default OngoingWebtoonsPage; 