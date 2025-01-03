import React from 'react';
import styles from './WebtoonAnalysis.module.css';

const WebtoonAnalysis = ({ analysisData }) => {
  return (
    <div className={styles['webtoon-analysis']}>
      <h2>웹툰 분석</h2>
      {analysisData ? (
        <div className={styles['analysis-content']}>
          <p>조회수: {analysisData.views}</p>
          <p>좋아요 수: {analysisData.likes}</p>
          <p>댓글 수: {analysisData.comments}</p>
        </div>
      ) : (
        <p>분석 데이터가 없습니다.</p>
      )}
    </div>
  );
};

export default WebtoonAnalysis; 