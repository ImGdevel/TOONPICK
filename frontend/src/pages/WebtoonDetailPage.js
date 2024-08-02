// src/pages/WebtoonDetailPage.js
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import WebtoonDetail from '../components/WebtoonDetail';
import { getWebtoonById } from '../services/webtoonService';

const WebtoonDetailPage = () => {
  const { id } = useParams();
  const [webtoon, setWebtoon] = useState(null);

  useEffect(() => {
    getWebtoonById(id).then((data) => setWebtoon(data));
  }, [id]);

  if (!webtoon) {
    return <div>Loading...</div>;
  }

  return <WebtoonDetail webtoon={webtoon} />;
};

export default WebtoonDetailPage;
