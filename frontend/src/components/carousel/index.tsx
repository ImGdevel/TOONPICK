import React, { useState } from 'react';
import styles from './style.module.css';

interface CarouselProps {
  images: string[];
}

const Carousel: React.FC<CarouselProps> = ({ images }) => {
  const [currentIndex, setCurrentIndex] = useState<number>(0);
  const [loadedImages, setLoadedImages] = useState<number[]>([]);

  const handleImageLoad = (index: number): void => {
    setLoadedImages((prevLoadedImages) => [...prevLoadedImages, index]);
  };

  const nextSlide = (): void => {
    setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
  };

  const prevSlide = (): void => {
    setCurrentIndex((prevIndex) =>
      prevIndex === 0 ? images.length - 1 : prevIndex - 1
    );
  };

  const goToSlide = (index: number): void => {
    setCurrentIndex(index);
  };

  return (
    <div className={styles.carousel}>
      <div
        className={styles.carouselInner}
        style={{ transform: `translateX(-${currentIndex * 100}%)` }}
      >
        {images.map((image, index) => (
          <div key={index} className={styles.carouselItem}>
            <img
              src={image}
              alt={`Slide ${index}`}
              onLoad={() => handleImageLoad(index)}
              style={{ display: loadedImages.includes(index) ? 'block' : 'none' }}
            />
            {!loadedImages.includes(index) && <div className={styles.imagePlaceholder}></div>}
          </div>
        ))}
      </div>
      <button className={`${styles.carouselControl} ${styles.prev}`} onClick={prevSlide}>
        &#10094;
      </button>
      <button className={`${styles.carouselControl} ${styles.next}`} onClick={nextSlide}>
        &#10095;
      </button>
      <div className={styles.carouselIndicators}>
        {images.map((_, index) => (
          <button
            key={index}
            className={`${styles.indicator} ${index === currentIndex ? styles.active : ''}`}
            onClick={() => goToSlide(index)}
          ></button>
        ))}
      </div>
    </div>
  );
};

export default Carousel; 