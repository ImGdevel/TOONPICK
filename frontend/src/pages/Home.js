// src/pages/Home.js
import React, { useState, useEffect } from 'react';
import Carousel from '../components/Carousel';
import SectionTitle from '../components/SectionTitle';
import WebtoonList from '../components/WebtoonList';
import { getWebtoons, getCarouselImages } from '../services/webtoonService';
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
      <SectionTitle title="툰픽의 PICK!" />
      <WebtoonList webtoons={webtoons} />
    </div>
  );
};

export default Home;
