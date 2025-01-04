// src/pages/NewWebtoonsPage.js
import React, { useState, useEffect } from 'react';
import WebtoonItem from '../components/WebtoonItem';
import styles from './NewWebtoonsPage.module.css'; 

const NewWebtoonsPage = () => {
  const [webtoons, setWebtoons] = useState([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchNewWebtoons = async () => {
      setLoading(true);
      // 실제 웹툰 데이터를 불러오는 로직이 이곳에 추가되어야 합니다.
      // 예: const response = await fetchNewWebtoonsFromAPI(page);
      // setWebtoons(response.data);

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
    <div className={styles['new-webtoons']}>
      <h1>신작 웹툰</h1>
      <div className={styles['webtoon-list']}>
        {webtoons.map((webtoon) => (
          <WebtoonItem key={webtoon.id} webtoon={webtoon} />
        ))}
      </div>
      {loading && <p>Loading...</p>}
    </div>
  );
};

export default NewWebtoonsPage;
