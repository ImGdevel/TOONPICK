import React, { useState, useEffect } from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import SectionTitle from '../components/SectionTitle';
import Carousel from '../components/Carousel';
import WebtoonList from '../components/WebtoonList';
import { getWebtoons, getCarouselImages } from '../services/webtoonService';
import styles from '../styles/Home.module.css';

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
    <div className={styles.container}>
      <Header />
      <main>
        <SectionTitle title="TOONPICK" />
        <Carousel images={carouselImages} />
        <SectionTitle title="툰픽의 PICK!" />
        <WebtoonList webtoons={webtoons} />
      </main>
      <Footer />
    </div>
  );
};

export default Home;
