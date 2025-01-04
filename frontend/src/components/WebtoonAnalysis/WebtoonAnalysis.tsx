import React from 'react';
import styles from './WebtoonAnalysis.module.css';

interface AnalysisData {
  views: number;
  likes: number;
  comments: number;
  averageRating?: number;
  weeklyRank?: number;
  genreRank?: number;
}

interface WebtoonAnalysisProps {
  analysisData: AnalysisData | null;
}

const WebtoonAnalysis: React.FC<WebtoonAnalysisProps> = ({ analysisData }) => {
  const formatNumber = (num: number): string => {
    return num.toLocaleString('ko-KR');
  };

  return (
    <div className={styles['webtoon-analysis']}>
      <h2>웹툰 분석</h2>
      {analysisData ? (
        <div className={styles['analysis-content']}>
          <p>조회수: {formatNumber(analysisData.views)}</p>
          <p>좋아요 수: {formatNumber(analysisData.likes)}</p>
          <p>댓글 수: {formatNumber(analysisData.comments)}</p>
          {analysisData.averageRating && (
            <p>평균 평점: {analysisData.averageRating.toFixed(2)}</p>
          )}
          {analysisData.weeklyRank && (
            <p>주간 순위: {analysisData.weeklyRank}위</p>
          )}
          {analysisData.genreRank && (
            <p>장르 내 순위: {analysisData.genreRank}위</p>
          )}
        </div>
      ) : (
        <p>분석 데이터가 없습니다.</p>
      )}
    </div>
  );
};

export default WebtoonAnalysis; 