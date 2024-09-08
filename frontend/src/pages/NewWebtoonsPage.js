// src/pages/NewWebtoonsPage.js
import React, { useState, useEffect } from 'react';
import WebtoonItem from '../components/WebtoonItem';
import { getFilteredWebtoons } from '../services/webtoonService';
import './NewWebtoonsPage.module.css';

const NewWebtoonsPage = () => {
  const [webtoons, setWebtoons] = useState([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchNewWebtoons = async () => {
      setLoading(true);
      const response = await getFilteredWebtoons({ statusFilter: '신작', dayFilter: '전체', genreFilter: [], page });
      setWebtoons((prevWebtoons) => [...prevWebtoons, ...response]);
      setLoading(false);
    };

    fetchNewWebtoons();
  }, [page]);

  const handleScroll = (e) => {
    if (window.innerHeight + document.documentElement.scrollTop === document.documentElement.offsetHeight) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  return (
    <div className="new-webtoons">
      <h1>신작 웹툰</h1>
      <div className="webtoon-list">
        {webtoons.map((webtoon) => (
          <WebtoonItem key={webtoon.id} webtoon={webtoon} />
        ))}
      </div>
      {loading && <p>Loading...</p>}
    </div>
  );
};

export default NewWebtoonsPage;
