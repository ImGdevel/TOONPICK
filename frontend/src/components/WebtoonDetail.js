// src/components/WebtoonDetail.js
import React from 'react';

const WebtoonDetail = ({ webtoon }) => (
  <div>
    <h2>{webtoon.title}</h2>
    <p>{webtoon.description}</p>
    <img src={webtoon.imageUrl} alt={webtoon.title} />
  </div>
);

export default WebtoonDetail;
