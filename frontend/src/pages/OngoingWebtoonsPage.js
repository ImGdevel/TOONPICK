// src/pages/OngoingWebtoonsPage.js
import React, { useEffect, useState } from 'react';
import WebtoonItem from '../components/WebtoonItem';
import { fetchWebtoonsByCategory } from '../services/webtoonService';
import './OngoingWebtoonsPage.module.css';

const OngoingWebtoonsPage = () => {
  const [webtoons, setWebtoons] = useState([]);

  useEffect(() => {
    fetchWebtoonsByCategory('ongoing').then(setWebtoons).catch(console.error);
  }, []);

  return (
    <div>
      <h1>연재 중 웹툰</h1>
      <div className="webtoon-list">
        {webtoons.map(webtoon => (
          <WebtoonItem key={webtoon.id} webtoon={webtoon} />
        ))}
      </div>
    </div>
  );
};

export default OngoingWebtoonsPage;
