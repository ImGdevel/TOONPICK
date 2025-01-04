// src/components/ExplorePage.js
import React, { useState, useEffect } from 'react';
import WebtoonItem from '../components/WebtoonItem';
import { getWebtoonsByDayOfWeek } from '../services/webtoonService';
import styles from './ExplorePage.module.css';

const ExplorePage = () => {
  const [statusFilter, setStatusFilter] = useState('전체');
  const [dayFilter, setDayFilter] = useState('전체');
  const [genreFilter, setGenreFilter] = useState([]);
  const [webtoons, setWebtoons] = useState([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchWebtoons = async () => {
      setLoading(true);

      // 요일 필터가 특정 요일일 때 getWebtoonByDayOfWeek 함수 호출
      if (dayFilter !== '전체' && dayFilter !== '매일' && dayFilter !== '열흘') {
        const response = await getWebtoonsByDayOfWeek(dayFilterToEnum(dayFilter));
        if (response.success) {
          setWebtoons(response.data); // 가져온 웹툰 데이터로 업데이트
        }
      } else {
        // 다른 조건 처리 로직이 있을 수 있습니다.
        // 예를 들어, 다른 필터 조건에 맞는 API 호출 로직을 추가할 수 있습니다.
      }

      setLoading(false);
    };

    fetchWebtoons();
  }, [statusFilter, dayFilter, genreFilter, page]);

  const handleScroll = (e) => {
    if (window.innerHeight + document.documentElement.scrollTop === document.documentElement.offsetHeight) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const toggleGenreFilter = (genre) => {
    setGenreFilter((prevGenreFilter) =>
      prevGenreFilter.includes(genre)
        ? prevGenreFilter.filter((g) => g !== genre)
        : [...prevGenreFilter, genre]
    );
  };

  const dayFilterToEnum = (day) => {
    const dayEnum = {
      '월': 'MONDAY',
      '화': 'TUESDAY',
      '수': 'WEDNESDAY',
      '목': 'THURSDAY',
      '금': 'FRIDAY',
      '토': 'SATURDAY',
      '일': 'SUNDAY'
    };
    return dayEnum[day];
  };

  return (
    <div className={styles.explore}>
      <div className={styles['filter-row']}>
        <div className={styles['filter-group']}>
          <button onClick={() => setStatusFilter('전체')}>전체</button>
          <button onClick={() => setStatusFilter('연재중')}>연재중</button>
          <button onClick={() => setStatusFilter('휴재')}>휴재</button>
          <button onClick={() => setStatusFilter('완결')}>완결</button>
        </div>
        <input type="text" placeholder="검색" />
      </div>

      <div className={styles['filter-row']}>
        <div className={styles['filter-group']}>
          {['월', '화', '수', '목', '금', '토', '일', '매일', '열흘'].map((day) => (
            <button key={day} onClick={() => day !== '매일' && day !== '열흘' && setDayFilter(day)}>
              {day}
            </button>
          ))}
        </div>
      </div>

      <div className={styles['filter-row']}>
        <div className={`${styles['filter-group']} ${styles['genre-filter']}`}>
          {['판타지', '액션', '드라마', '무협', '개그', '일상', '미스테리', '스포츠'].map((genre) => (
            <button
              key={genre}
              onClick={() => toggleGenreFilter(genre)}
              className={genreFilter.includes(genre) ? styles.active : ''}
            >
              {genre}
            </button>
          ))}
        </div>
      </div>

      <div className={styles['webtoon-list']}>
        {webtoons.map((webtoon) => (
          <WebtoonItem key={webtoon.id} webtoon={webtoon} />
        ))}
      </div>
      {loading && <p>Loading...</p>}
    </div>
  );
};

export default ExplorePage;
