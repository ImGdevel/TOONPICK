import React, { useState, useEffect, useRef } from 'react';
import WebtoonService from '@services/webtoonService';
import WebtoonGrid from '@components/WebtoonGrid';
import styles from './CompletedWebtoonsPage.module.css';
import{ Webtoon } from '@models/webtoon';

const CompletedWebtoonsPage: React.FC = () => {
  const [webtoons, setWebtoons] = useState<Webtoon[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const observer = useRef<IntersectionObserver | null>(null);
  const lastWebtoonRef = useRef<HTMLDivElement | null>(null); // 마지막 웹툰 요소 참조

  const fetchCompletedWebtoons = async (page: number) => {
    const size = 20; // 페이지 크기 설정
    try {
      const response = await WebtoonService.getCompletedWebtoons(page, size);
      setWebtoons((prev) => [...prev, ...(response.data || [])]);
      setTotalPages(Math.ceil((response.total || 0) / size));
      setIsLoading(false);
    } catch (error) {
      setError('완결 웹툰을 불러오는데 실패했습니다.');
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCompletedWebtoons(currentPage);
  }, [currentPage]);

  useEffect(() => {
    if (observer.current) {
      observer.current.disconnect(); // 이전 관찰 중지
    }

    observer.current = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting && currentPage < totalPages) {
          setCurrentPage((prev) => prev + 1); // 다음 페이지 로드
        }
      });
    });

    if (lastWebtoonRef.current) {
      observer.current.observe(lastWebtoonRef.current); // 마지막 웹툰 요소 관찰
    }

    return () => {
      if (observer.current && lastWebtoonRef.current) {
        observer.current.unobserve(lastWebtoonRef.current); // 언마운트 시 관찰 중지
      }
    };
  }, [lastWebtoonRef.current, currentPage, totalPages]);

  return (
    <div className={styles.completedWebtoonsPage}>
      <h1>완결 웹툰</h1>
      {isLoading ? (
        <p>로딩중...</p>
      ) : (
        <WebtoonGrid webtoons={webtoons} />
      )}
      <div ref={lastWebtoonRef} /> {/* 마지막 웹툰 요소 */}
    </div>
  );
};

export default CompletedWebtoonsPage; 