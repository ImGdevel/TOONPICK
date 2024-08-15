// src/pages/WebtoonDetailPage.js
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getWebtoonById } from '../services/webtoonService';
import './WebtoonDetailPage.css';

const WebtoonDetailPage = () => {
  const { id } = useParams();
  const [webtoon, setWebtoon] = useState("null");
  const [activeTab, setActiveTab] = useState('info');

  const handleTabChange = (tab) => {
    setActiveTab(tab);
  };

  useEffect(() => {
    const fetchWebtoon = async () => {
      const data = await getWebtoonById(id);
      setWebtoon(data);
    };

    fetchWebtoon();
  }, [id]);

  if (!webtoon) return <div>Loading...</div>;

  return (
    <div className="webtoon-detail-page">
      {/* 상단: 웹툰 기본 정보 */}
      <div className="webtoon-info">
        <div className="webtoon-image">
          <img src="https://via.placeholder.com/460x623" alt="웹툰 이미지" />
        </div>
        <div className="webtoon-details">
          <div className="buttons">
            <button className="bookmark-button">북마크</button>
            <button className="heart-button">❤</button>
          </div>
          <div className="webtoon-title">
            웹툰 제목
          </div>
          <div className="webtoon-meta">
            작가명1, 작가명2 | ★4.5
          </div>
          <div className="extra-info">
            {/* 나중에 사용할 빈 공간 */}
          </div>
        </div>
      </div>

      {/* 중단: 컨텐츠 */}
      <div className="webtoon-content">
        <div className="content-tabs">
          <button onClick={() => handleTabChange('info')} className={activeTab === 'info' ? 'active' : ''}>정보</button>
          <button onClick={() => handleTabChange('list')} className={activeTab === 'list' ? 'active' : ''}>연재 리스트</button>
          <button onClick={() => handleTabChange('analysis')} className={activeTab === 'analysis' ? 'active' : ''}>분석</button>
          <button onClick={() => handleTabChange('reviews')} className={activeTab === 'reviews' ? 'active' : ''}>평가 리스트</button>
        </div>
        
        <div className="content-body">
          {activeTab === 'info' && <div>웹툰 정보 페이지</div>}
          {activeTab === 'list' && <div>연재 리스트 페이지</div>}
          {activeTab === 'analysis' && <div>분석 페이지</div>}
          {activeTab === 'reviews' && <div>평가 리스트 페이지</div>}
        </div>
      </div>

      {/* 하단: 비슷한 웹툰 리스트 */}
      <div className="similar-webtoons">
        <h3>비슷한 웹툰</h3>
        <div className="similar-webtoons-list">
          <div className="similar-webtoon-item">웹툰1</div>
          <div className="similar-webtoon-item">웹툰2</div>
          <div className="similar-webtoon-item">웹툰3</div>
          <div className="similar-webtoon-item">웹툰4</div>
          <div className="similar-webtoon-item">웹툰5</div>
        </div>
      </div>
    </div>
  );
};

export default WebtoonDetailPage;
