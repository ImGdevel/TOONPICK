// src/pages/Home.js
import React, { useState, useEffect} from 'react';
import Carousel from '../components/Carousel';
import WebtoonList from '../components/WebtoonList';
import { getCarouselImages } from '../services/webtoonService';
import './Home.module.css';

const Home = () => {
  const [webtoons, setWebtoons] = useState([]);
  const [carouselImages, setCarouselImages] = useState([]);

  useEffect(() => {
    const fetchWebtoons = async () => {

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
      <Carousel images={carouselImages} />

      <WebtoonList webtoons={webtoons} />
      

    </div>
  );
};

export default Home;
