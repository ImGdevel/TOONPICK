// src/components/WebtoonList/WebtoonList.js
import React from 'react';
import './WebtoonList.css';

const WebtoonList = ({ webtoons }) => {
  return (
    <div className="webtoon-list">
      {webtoons.map((webtoon) => (
        <div key={webtoon.id} className="webtoon-item">
          <img src={webtoon.imageUrl} alt={webtoon.title} />
          <div className="webtoon-info">
            <h3>{webtoon.title}</h3>
            <p>{webtoon.author}</p>
            <p>Rating: {webtoon.rating}</p>
          </div>
        </div>
      ))}
    </div>
  );
};

export default WebtoonList;
