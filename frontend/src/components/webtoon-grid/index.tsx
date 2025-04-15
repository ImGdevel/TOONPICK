import React, { useState, useEffect, useRef } from 'react';
import styles from './style.module.css';
import { Webtoon } from '@models/webtoon';
import WebtoonCard from '@components/webtoon-card';

interface WebtoonGridProps {
  webtoons: Webtoon[];
  loading: boolean;
  onWebtoonClick: (webtoon: Webtoon) => void;
  lastWebtoonRef?: React.RefObject<HTMLDivElement>;
}

const WebtoonGrid: React.FC<WebtoonGridProps> = ({ webtoons, loading, onWebtoonClick, lastWebtoonRef }) => {
  const [visibleIndices, setVisibleIndices] = useState<number[]>([]);
  const gridColumns = 5;
  const prevWebtoonsLength = useRef(webtoons.length);

  useEffect(() => {
    if (webtoons.length > 0) {
      // 이전에 로드된 카드 수를 유지하고 새로 추가된 카드만 로드
      const newIndices = Array.from(
        { length: webtoons.length - prevWebtoonsLength.current },
        (_, i) => prevWebtoonsLength.current + i
      );
      
      if (newIndices.length > 0) {
        setVisibleIndices(prev => [...prev, ...newIndices]);
      }
      
      prevWebtoonsLength.current = webtoons.length;
    }
  }, [webtoons]);

  const renderSkeleton = () => {
    return Array(10).fill(0).map((_, index) => (
      <div key={index} className={styles.skeletonCard}>
        <div className={styles.skeletonThumbnail} />
        <div className={styles.skeletonTitle} />
        <div className={styles.skeletonMeta} />
      </div>
    ));
  };

  return (
    <div className={styles.grid}>
      {loading ? (
        renderSkeleton()
      ) : (
        webtoons.map((webtoon, index) => (
          <div
            key={webtoon.id}
            className={`${styles.gridItem} ${visibleIndices.includes(index) ? styles.fadeIn : ''}`}
            ref={index === webtoons.length - 1 ? lastWebtoonRef : null}
          >
            <WebtoonCard 
              webtoon={webtoon}
              onClick={() => onWebtoonClick(webtoon)}
            />
          </div>
        ))
      )}
    </div>
  );
};

export default WebtoonGrid;
