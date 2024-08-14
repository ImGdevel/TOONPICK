// src/components/WebtoonItem.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './WebtoonItem.css';

const WebtoonItem = ({ webtoon }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/webtoon/${webtoon.id}`);
  };

  return (
    <div className="webtoon-item" onClick={handleClick}>
      <img src={webtoon.imageUrl} alt={webtoon.title} />
      <div className="webtoon-info">
        <h3>{webtoon.title}</h3>
        <p>{webtoon.author}</p>
        <p>Rating: {webtoon.rating}</p>
      </div>
    </div>
  );
};

export default WebtoonItem;
