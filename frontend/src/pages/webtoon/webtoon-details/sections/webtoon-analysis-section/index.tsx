import React from 'react';
import { Webtoon } from '@models/webtoon';
import styles from './style.module.css';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  LineChart,
  Line
} from 'recharts';

interface WebtoonAnalysisSectionProps {
  webtoon: Webtoon;
}

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

const WebtoonAnalysisSection: React.FC<WebtoonAnalysisSectionProps> = ({ webtoon }) => {
  const analysisData = webtoon.analysisData || {
    totalViews: 0,
    totalSubscribers: 0,
    averageViewTime: 0,
    completionRate: 0,
    readerDemographics: {
      ageGroups: [],
      genderDistribution: []
    },
    growthMetrics: {
      dailyViews: [],
    },
    contentAnalysis: {
      genreDistribution: [],
      characterPopularity: []
    },
    predictionMetrics: {
      expectedGrowth: 0,
      retentionRate: 0,
      churnRate: 0
    }
  };

  return (
    <section className={styles.section}>
      <h2 className={styles.sectionTitle}>웹툰 분석</h2>
      
      {/* 기본 통계 */}
      <div className={styles.statsGrid}>
        <div className={styles.statCard}>
          <h3>총 조회수</h3>
          <p className={styles.statValue}>{analysisData.totalViews > 0 ? analysisData.totalViews.toLocaleString() : '분석 데이터가 없습니다.'}</p>
        </div>
        <div className={styles.statCard}>
          <h3>구독자 수</h3>
          <p className={styles.statValue}>{analysisData.totalSubscribers > 0 ? analysisData.totalSubscribers.toLocaleString() : '분석 데이터가 없습니다.'}</p>
        </div>
        <div className={styles.statCard}>
          <h3>평균 독서 시간</h3>
          <p className={styles.statValue}>{analysisData.averageViewTime > 0 ? `${analysisData.averageViewTime}분` : '분석 데이터가 없습니다.'}</p>
        </div>
        <div className={styles.statCard}>
          <h3>완독률</h3>
          <p className={styles.statValue}>{analysisData.completionRate > 0 ? `${analysisData.completionRate}%` : '분석 데이터가 없습니다.'}</p>
        </div>
      </div>

      {/* 독자 통계 */}
      <div className={styles.chartContainer}>
        <h3>독자 통계</h3>
        <div className={styles.chartGrid}>
          <div className={styles.chart}>
            <h4>연령대 분포</h4>
            {analysisData.readerDemographics.ageGroups.length > 0 ? (
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={analysisData.readerDemographics.ageGroups}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="age" />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="percentage" fill="#8884d8" />
                </BarChart>
              </ResponsiveContainer>
            ) : (
              <p>분석 데이터가 없습니다.</p>
            )}
          </div>
          <div className={styles.chart}>
            <h4>성별 분포</h4>
            {analysisData.readerDemographics.genderDistribution.length > 0 ? (
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={analysisData.readerDemographics.genderDistribution}
                    dataKey="percentage"
                    nameKey="gender"
                    cx="50%"
                    cy="50%"
                    outerRadius={100}
                    label
                  >
                    {analysisData.readerDemographics.genderDistribution.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            ) : (
              <p>분석 데이터가 없습니다.</p>
            )}
          </div>
        </div>
      </div>

      {/* 성장 지표 */}
      <div className={styles.chartContainer}>
        <h3>성장 지표</h3>
        <div className={styles.chart}>
          {analysisData.growthMetrics.dailyViews.length > 0 ? (
            <ResponsiveContainer width="100%" height={400}>
              <LineChart data={analysisData.growthMetrics.dailyViews}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="count" stroke="#8884d8" name="일일 조회수" />
              </LineChart>
            </ResponsiveContainer>
          ) : (
            <p>분석 데이터가 없습니다.</p>
          )}
        </div>
      </div>

      {/* 콘텐츠 분석 */}
      <div className={styles.chartContainer}>
        <h3>콘텐츠 분석</h3>
        <div className={styles.chartGrid}>
          <div className={styles.chart}>
            <h4>장르 분포</h4>
            {analysisData.contentAnalysis.genreDistribution.length > 0 ? (
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={analysisData.contentAnalysis.genreDistribution}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="genre" />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="percentage" fill="#82ca9d" />
                </BarChart>
              </ResponsiveContainer>
            ) : (
              <p>분석 데이터가 없습니다.</p>
            )}
          </div>
          <div className={styles.chart}>
            <h4>인기 캐릭터</h4>
            {analysisData.contentAnalysis.characterPopularity.length > 0 ? (
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={analysisData.contentAnalysis.characterPopularity}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="character" />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="popularity" fill="#ffc658" />
                </BarChart>
              </ResponsiveContainer>
            ) : (
              <p>분석 데이터가 없습니다.</p>
            )}
          </div>
        </div>
      </div>

      {/* 예측 분석 */}
      <div className={styles.predictionContainer}>
        <h3>예측 분석</h3>
        <div className={styles.predictionGrid}>
          <div className={styles.predictionCard}>
            <h4>예상 성장률</h4>
            <p className={styles.predictionValue}>{analysisData.predictionMetrics.expectedGrowth > 0 ? `${analysisData.predictionMetrics.expectedGrowth}%` : '분석 데이터가 없습니다.'}</p>
          </div>
          <div className={styles.predictionCard}>
            <h4>유지율</h4>
            <p className={styles.predictionValue}>{analysisData.predictionMetrics.retentionRate > 0 ? `${analysisData.predictionMetrics.retentionRate}%` : '분석 데이터가 없습니다.'}</p>
          </div>
          <div className={styles.predictionCard}>
            <h4>이탈률</h4>
            <p className={styles.predictionValue}>{analysisData.predictionMetrics.churnRate > 0 ? `${analysisData.predictionMetrics.churnRate}%` : '분석 데이터가 없습니다.'}</p>
          </div>
        </div>
      </div>
    </section>
  );
};

export default WebtoonAnalysisSection;
