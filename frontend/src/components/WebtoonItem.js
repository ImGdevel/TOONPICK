// src/components/WebtoonItem.js
import React from 'react';
import './WebtoonItem.css';

const WebtoonItem = ({ webtoon }) => (
  <div className="webtoon-item">
    <img src={webtoon.imageUrl} alt={webtoon.title} />
    <div className="webtoon-info">
      <h3>{webtoon.title}</h3>
      <p>{webtoon.author}</p>
      <p>Rating: {webtoon.rating}</p>
    </div>
  </div>
);

export default WebtoonItem;
