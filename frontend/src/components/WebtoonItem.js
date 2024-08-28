// src/components/WebtoonItem.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './WebtoonItem.css';

const WebtoonItem = ({ webtoon }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/webtoon/${webtoon.id}`);
  };

  // 저자 목록을 쉼표로 구분된 문자열로 변환
  const authors = webtoon.authors ? Array.from(webtoon.authors).map(author => author.name).join(', ') : 'Unknown Author';

  // 평점을 안전하게 처리하고 기본값을 제공
  const averageRating = webtoon.averageRating !== undefined ? webtoon.averageRating.toFixed(1) : 'N/A';

  // 제목이 30자를 넘으면 "..."으로 처리
  const truncatedTitle = webtoon.title.length > 30 ? `${webtoon.title.substring(0, 30)}...` : webtoon.title;

  return (
    <div className="webtoon-item" onClick={handleClick}>
      <div className="thumbnail-container">
        <img src={webtoon.thumbnailUrl} alt={webtoon.title} className="thumbnail-image" />
      </div>
      <div className="webtoon-info">
        <span className="webtoon-title">{truncatedTitle}</span>
        <span className="webtoon-author">{authors}</span>
        <span className="webtoon-rating">Rating: {averageRating}</span>
      </div>
    </div>
  );
};

export default WebtoonItem;
