// src/components/WebtoonItem.js
import React from 'react';
import './WebtoonItem.css';

const WebtoonItem = ({ webtoon }) => (
  <div className="webtoon-item">
    <img src={webtoon.imageUrl} alt={webtoon.title} className="webtoon-image" />
    <h3>{webtoon.title}</h3>
    <p>{webtoon.author}</p>
    <p>{webtoon.rating} ⭐</p>
  </div>
);

export default WebtoonItem;
