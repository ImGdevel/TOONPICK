import React, { useState, useEffect } from 'react';
import styles from './style.module.css';

interface CarouselItem {
  imageUrl: string;
  link: string;
}

interface CarouselProps {
  items: CarouselItem[];
}

const Carousel: React.FC<CarouselProps> = ({ items }) => {
  const [currentIndex, setCurrentIndex] = useState<number>(0);
  const [isTransitioning, setIsTransitioning] = useState<boolean>(false);

  const handleDotClick = (index: number) => {
    if (isTransitioning) return;
    setIsTransitioning(true);
    setCurrentIndex(index);
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsTransitioning(false);
    }, 500); // transition duration과 동일하게 설정
    return () => clearTimeout(timer);
  }, [currentIndex]);

  return (
    <div className={styles.carouselContainer}>
      <div 
        className={styles.carouselWrapper}
        style={{ 
          transform: `translateX(-${currentIndex * 100}%)`,
          transition: isTransitioning ? 'transform 0.5s ease-in-out' : 'none'
        }}
      >
        {items.map((item, index) => (
          <a 
            key={index} 
            href={item.link} 
            className={styles.carouselItem}
            target="_blank"
            rel="noopener noreferrer"
          >
            <img 
              src={item.imageUrl} 
              alt={`Slide ${index + 1}`}
              className={styles.carouselImage}
            />
          </a>
        ))}
      </div>
      
      <div className={styles.dotsContainer}>
        {items.map((_, index) => (
          <button
            key={index}
            className={`${styles.dot} ${index === currentIndex ? styles.activeDot : ''}`}
            onClick={() => handleDotClick(index)}
            aria-label={`Go to slide ${index + 1}`}
          />
        ))}
      </div>
    </div>
  );
};

export default Carousel; 