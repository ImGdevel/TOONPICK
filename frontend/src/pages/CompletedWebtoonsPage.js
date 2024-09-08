import React, { useState, useEffect } from 'react';
import WebtoonItem from '../components/WebtoonItem';
import { getFilteredWebtoons } from '../services/webtoonService';

const CompletedWebtoonsPage = () => {
  const [webtoons, setWebtoons] = useState([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchWebtoons = async () => {
      setLoading(true);
      const newWebtoons = await getFilteredWebtoons({ statusFilter: '완결', dayFilter: '전체', genreFilter: [], page });
      setWebtoons((prevWebtoons) => [...prevWebtoons, ...newWebtoons]);
      setLoading(false);
    };

    fetchWebtoons();
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
    <div>
      <h1>완결된 웹툰</h1>
      <div className="webtoon-list">
        {webtoons.map((webtoon) => (
          <WebtoonItem key={webtoon.id} webtoon={webtoon} />
        ))}
      </div>
      {loading && <p>Loading...</p>}
    </div>
  );
};

export default CompletedWebtoonsPage;
