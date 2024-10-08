import React, { useState, useEffect } from 'react';
import WebtoonItem from '../components/WebtoonItem';
import { getWebtoonsByDayOfWeek, getWebtoons } from '../services/webtoonService';
import styles from './OngoingWebtoonsPage.module.css';

const OngoingWebtoonsPage = () => {
  const [dayFilter, setDayFilter] = useState(''); // 기본값을 빈 문자열로 설정
  const [webtoons, setWebtoons] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // 현재 날짜의 요일 계산
    const today = new Date();
    const dayNames = ['일', '월', '화', '수', '목', '금', '토'];
    const currentDay = dayNames[today.getDay()]; // 현재 요일

    // 기본 필터값을 현재 요일로 설정
    setDayFilter(currentDay);
  }, []);

  useEffect(() => {
    const fetchWebtoons = async () => {
      setLoading(true);

      // 현재 요일에 해당하는 웹툰 가져오기
      if (dayFilter) {
        if (dayFilter === '전체') {
          // "전체"일 때 모든 웹툰 가져오기
          const response = await getWebtoons();
          if (response.success) {
            setWebtoons(response.data);
          } else {
            setWebtoons([]);
          }
        } else {
          // 특정 요일의 웹툰 가져오기
          const response = await getWebtoonsByDayOfWeek(dayFilterToEnum(dayFilter));
          if (response.success) {
            setWebtoons(response.data);
          } else {
            setWebtoons([]);
          }
        }
      }

      setLoading(false);
    };

    fetchWebtoons();
  }, [dayFilter]);

  const dayFilterToEnum = (day) => {
    const dayEnum = {
      '월': 'MONDAY',
      '화': 'TUESDAY',
      '수': 'WEDNESDAY',
      '목': 'THURSDAY',
      '금': 'FRIDAY',
      '토': 'SATURDAY',
      '일': 'SUNDAY',
    };
    return dayEnum[day];
  };

  return (
    <div className={styles['ongoing-webtoons']}>
      <h1>연재 중 웹툰</h1>
      <div className={styles['filter-row']}>
        <div className={styles['filter-group']}>
          {/* 요일 필터 버튼 */}
          {['전체', '월', '화', '수', '목', '금', '토', '일'].map((day) => (
            <button
              key={day}
              onClick={() => setDayFilter(day)}
              className={dayFilter === day ? styles['active'] : ''}
            >
              {day}
            </button>
          ))}
        </div>
      </div>

      {/* 웹툰 목록 */}
      <div className={styles['webtoon-list']}>
        {webtoons.map((webtoon) => (
          <WebtoonItem key={webtoon.id} webtoon={webtoon} />
        ))}
      </div>

      {/* 로딩 상태 표시 */}
      {loading && <p>Loading...</p>}
    </div>
  );
};

export default OngoingWebtoonsPage;
