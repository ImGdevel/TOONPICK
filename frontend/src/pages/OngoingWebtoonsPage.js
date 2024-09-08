// src/pages/OngoingWebtoonsPage.js

import React, { useState, useEffect } from 'react';
import WebtoonItem from '../components/WebtoonItem';
import { getWebtoonsByDayOfWeek } from '../services/webtoonService';
import './OngoingWebtoonsPage.module.css';

const OngoingWebtoonsPage = () => {
  const [dayFilter, setDayFilter] = useState('전체'); // 요일 필터 상태
  const [webtoons, setWebtoons] = useState([]); // 연재 중 웹툰 상태
  const [loading, setLoading] = useState(false); // 로딩 상태

  useEffect(() => {
    const fetchWebtoons = async () => {
      setLoading(true); // 로딩 시작

      if (dayFilter !== '전체') {
        // 요일 필터가 "전체"가 아닐 때만 해당 요일의 웹툰을 가져옴
        const response = await getWebtoonsByDayOfWeek(dayFilterToEnum(dayFilter));
        if (response.success) {
          setWebtoons(response.data);
        } else {
          setWebtoons([]);
        }
      } else {
        setWebtoons([]); // 기본적으로 필터가 없으면 빈 리스트로 설정
      }

      setLoading(false); // 로딩 끝
    };

    fetchWebtoons();
  }, [dayFilter]); // dayFilter가 변경될 때마다 호출

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
    <div className="ongoing-webtoons">
      <h1>연재 중 웹툰</h1>
      <div className="filter-row">
        <div className="filter-group">
          {/* 요일 필터 버튼 */}
          {['전체', '월', '화', '수', '목', '금', '토', '일'].map((day) => (
            <button
              key={day}
              onClick={() => setDayFilter(day)}
              className={dayFilter === day ? 'active' : ''}
            >
              {day}
            </button>
          ))}
        </div>
      </div>

      {/* 웹툰 목록 */}
      <div className="webtoon-list">
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
