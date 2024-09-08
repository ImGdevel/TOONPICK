import React, { useState, useEffect } from 'react';
import WebtoonItem from '../components/WebtoonItem';
import { getWebtoonByDayOfWeek } from '../services/webtoonService';
import './ExplorePage.css';

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
        console.log("why?");
        const response = await getWebtoonByDayOfWeek(dayFilterToEnum(dayFilter));
        if (response.success) {
          setWebtoons(response.data); // 가져온 웹툰 데이터로 업데이트
        }
      } else {

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

  // 요일을 Enum 형태로 매핑
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
    <div className="explore">
      <div className="filter-row">
        <div className="filter-group">
          <button onClick={() => setStatusFilter('전체')}>전체</button>
          <button onClick={() => setStatusFilter('연재중')}>연재중</button>
          <button onClick={() => setStatusFilter('휴재')}>휴재</button>
          <button onClick={() => setStatusFilter('완결')}>완결</button>
        </div>
        <input type="text" placeholder="검색" />
      </div>

      <div className="filter-row">
        <div className="filter-group">
          {['월', '화', '수', '목', '금', '토', '일', '매일', '열흘'].map((day) => (
            <button key={day} onClick={() => day !== '매일' && day !== '열흘' && setDayFilter(day)}>
              {day}
            </button>
          ))}
        </div>
      </div>

      <div className="filter-row">
        <div className="filter-group genre-filter">
          {['판타지', '액션', '드라마', '무협', '개그', '일상', '미스테리', '스포츠'].map((genre) => (
            <button
              key={genre}
              onClick={() => toggleGenreFilter(genre)}
              className={genreFilter.includes(genre) ? 'active' : ''}
            >
              {genre}
            </button>
          ))}
        </div>
      </div>

      <div className="webtoon-list">
        {webtoons.map((webtoon) => (
          <WebtoonItem key={webtoon.id} webtoon={webtoon} />
        ))}
      </div>
      {loading && <p>Loading...</p>}
    </div>
  );
};

export default ExplorePage;
