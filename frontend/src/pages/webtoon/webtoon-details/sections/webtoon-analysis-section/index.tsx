import React from 'react';
import styles from './style.module.css';
import { Webtoon } from '@models/webtoon';
import { Chart } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement
);

interface WebtoonAnalysisSectionProps {
  webtoon: Webtoon;
}

const WebtoonAnalysisSection: React.FC<WebtoonAnalysisSectionProps> = ({ webtoon }) => {
  const { analysisData } = webtoon;

  if (!analysisData) {
    return null;
  }

  const genreChartData = {
    labels: analysisData.genreDistribution.map(item => item.genre),
    datasets: [
      {
        label: '장르 분포',
        data: analysisData.genreDistribution.map(item => item.percentage),
        backgroundColor: [
          'rgba(255, 99, 132, 0.5)',
          'rgba(54, 162, 235, 0.5)',
          'rgba(255, 206, 86, 0.5)',
          'rgba(75, 192, 192, 0.5)',
          'rgba(153, 102, 255, 0.5)',
        ],
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(75, 192, 192, 1)',
          'rgba(153, 102, 255, 1)',
        ],
        borderWidth: 1,
      },
    ],
  };

  const audienceChartData = {
    labels: analysisData.audienceAge.map(item => item.age),
    datasets: [
      {
        label: '연령대 분포',
        data: analysisData.audienceAge.map(item => item.percentage),
        backgroundColor: 'rgba(54, 162, 235, 0.5)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1,
      },
    ],
  };

  const genderChartData = {
    labels: analysisData.genderDistribution.map(item => item.gender),
    datasets: [
      {
        data: analysisData.genderDistribution.map(item => item.percentage),
        backgroundColor: [
          'rgba(255, 99, 132, 0.5)',
          'rgba(54, 162, 235, 0.5)',
        ],
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
        ],
        borderWidth: 1,
      },
    ],
  };

  return (
    <section className={styles.analysisSection}>
      <h2 className={styles.sectionTitle}>웹툰 분석</h2>
      
      <div className={styles.chartContainer}>
        <div className={styles.chartWrapper}>
          <h3>장르 분포</h3>
          <div className={styles.chart}>
            <Chart type="bar" data={genreChartData} />
          </div>
        </div>

        <div className={styles.chartWrapper}>
          <h3>연령대 분포</h3>
          <div className={styles.chart}>
            <Chart type="bar" data={audienceChartData} />
          </div>
        </div>

        <div className={styles.chartWrapper}>
          <h3>성별 분포</h3>
          <div className={styles.chart}>
            <Chart type="pie" data={genderChartData} />
          </div>
        </div>
      </div>

      <div className={styles.metricsContainer}>
        <div className={styles.metric}>
          <h4>플롯 복잡도</h4>
          <p>{analysisData.plotComplexity}/10</p>
        </div>
        <div className={styles.metric}>
          <h4>캐릭터 발전도</h4>
          <p>{analysisData.characterDevelopment}/10</p>
        </div>
        <div className={styles.metric}>
          <h4>세계관 구축도</h4>
          <p>{analysisData.worldBuilding}/10</p>
        </div>
        <div className={styles.metric}>
          <h4>전개 속도</h4>
          <p>{analysisData.pacing}/10</p>
        </div>
        <div className={styles.metric}>
          <h4>대화 품질</h4>
          <p>{analysisData.dialogueQuality}/10</p>
        </div>
        <div className={styles.metric}>
          <h4>작화 스타일</h4>
          <p>{analysisData.artStyle}/10</p>
        </div>
      </div>
    </section>
  );
};

export default WebtoonAnalysisSection; 