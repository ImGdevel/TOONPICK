// src/pages/Home.js
import React, { useState, useEffect} from 'react';
import Carousel from '../components/Carousel';
import SectionTitle from '../components/SectionTitle';
import WebtoonList from '../components/WebtoonList';
import { getWebtoons, getCarouselImages } from '../services/webtoonService';
import { Link } from 'react-router-dom'; // Link 추가
import './Home.css';

const Home = () => {
  const [webtoons, setWebtoons] = useState([]);
  const [carouselImages, setCarouselImages] = useState([]);

  useEffect(() => {
    const fetchWebtoons = async () => {
      const webtoonData = await getWebtoons();
      setWebtoons(webtoonData);
    };

    const fetchCarouselImages = async () => {
      const imageData = await getCarouselImages();
      setCarouselImages(imageData);
    };

    fetchWebtoons();
    fetchCarouselImages();
  }, []);

  return (
    <div className="home container">
      <SectionTitle title="TOONPICK" />
      <Carousel images={carouselImages} />

      <WebtoonList webtoons={webtoons} />
      
      {/* 탐색 페이지로 이동하는 버튼 추가 */}
      <div className="explore-button-container">
        <Link to="/explore">
          <button className="explore-button">탐색 페이지로 가기</button>
        </Link>
      </div>
    </div>
  );
};

export default Home;
