import React from 'react';
import { Webtoon, WebtoonAnalysisData } from '@models/webtoon';
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

interface AgeGroup {
  age: string;
  percentage: number;
  color: string;
}

interface GenderDistribution {
  gender: string;
  percentage: number;
  color: string;
}

interface DailyView {
  date: string;
  count: number;
  trend: 'up' | 'down';
}

interface GenreDistribution {
  genre: string;
  percentage: number;
  color: string;
}

interface CharacterPopularity {
  character: string;
  popularity: number;
  color: string;
}

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

// 색상 매핑 함수들
const getColorForAgeGroup = (age: string): string => {
  const colorMap: Record<string, string> = {
    '10대': '#FF6B6B',
    '20대': '#4ECDC4',
    '30대': '#45B7D1',
    '40대': '#96CEB4',
    '50대 이상': '#FFEEAD'
  };
  return colorMap[age] || '#8884D8';
};

const getColorForGender = (gender: string): string => {
  return gender === '남성' ? '#4ECDC4' : '#FF6B6B';
};

const getColorForGenre = (genre: string): string => {
  const colorMap: Record<string, string> = {
    '로맨스': '#FF6B6B',
    '판타지': '#4ECDC4',
    '액션': '#45B7D1',
    '일상': '#96CEB4',
    '스포츠': '#FFEEAD'
  };
  return colorMap[genre] || '#8884D8';
};

// 차트 데이터 변환 함수
const transformToChartData = (data: WebtoonAnalysisData) => {
  return {
    ...data,
    readerDemographics: {
      ...data.readerDemographics,
      ageGroups: data.readerDemographics.ageGroups.map(group => ({
        ...group,
        color: getColorForAgeGroup(group.age)
      })),
      genderDistribution: data.readerDemographics.genderDistribution.map(dist => ({
        ...dist,
        color: getColorForGender(dist.gender)
      }))
    },
    contentAnalysis: {
      ...data.contentAnalysis,
      genreDistribution: data.contentAnalysis.genreDistribution.map(genre => ({
        ...genre,
        color: getColorForGenre(genre.genre)
      })),
      characterPopularity: data.contentAnalysis.characterPopularity.map(char => ({
        ...char,
        color: getColorForGenre(char.character)
      }))
    }
  };
};

// 더미 데이터 생성 함수
const generateDummyData = (): WebtoonAnalysisData => {
  return {
    totalViews: 1850000,
    totalSubscribers: 65000,
    averageViewTime: 18,
    completionRate: 92,
    readerDemographics: {
      ageGroups: [
        { age: '10대', percentage: 28 },
        { age: '20대', percentage: 42 },
        { age: '30대', percentage: 18 },
        { age: '40대', percentage: 8 },
        { age: '50대 이상', percentage: 4 }
      ],
      genderDistribution: [
        { gender: '남성', percentage: 55 },
        { gender: '여성', percentage: 45 }
      ],
      regionDistribution: [
        { region: '서울', percentage: 35 },
        { region: '경기', percentage: 25 },
        { region: '인천', percentage: 10 },
        { region: '부산', percentage: 8 },
        { region: '기타', percentage: 22 }
      ]
    },
    ratingDistribution: [
      { rating: 1, count: 50 },
      { rating: 2, count: 100 },
      { rating: 3, count: 300 },
      { rating: 4, count: 500 },
      { rating: 5, count: 800 }
    ],
    reviewSentiment: {
      positive: 65,
      neutral: 25,
      negative: 10
    },
    contentAnalysis: {
      genreDistribution: [
        { genre: '로맨스', percentage: 32 },
        { genre: '판타지', percentage: 28 },
        { genre: '액션', percentage: 22 },
        { genre: '일상', percentage: 12 },
        { genre: '스포츠', percentage: 6 }
      ],
      tagDistribution: [
        { tag: '힐링', count: 1200 },
        { tag: '성장', count: 800 },
        { tag: '감동', count: 600 },
        { tag: '코믹', count: 400 },
        { tag: '드라마', count: 300 }
      ],
      characterPopularity: [
        { character: '주인공', popularity: 88 },
        { character: '히로인', popularity: 82 },
        { character: '라이벌', popularity: 75 },
        { character: '조력자', popularity: 68 },
        { character: '악당', popularity: 65 }
      ]
    },
    growthMetrics: {
      dailyViews: Array.from({ length: 30 }, (_, i) => ({
        date: `3/${i + 1}`,
        count: Math.floor(Math.random() * 8000) + 2000
      })),
      dailySubscribers: Array.from({ length: 30 }, (_, i) => ({
        date: `3/${i + 1}`,
        count: Math.floor(Math.random() * 200) + 50
      })),
      dailyComments: Array.from({ length: 30 }, (_, i) => ({
        date: `3/${i + 1}`,
        count: Math.floor(Math.random() * 100) + 20
      }))
    },
    platformComparison: [
      { platform: '네이버', averageRating: 4.5, totalViews: 1200000 },
      { platform: '카카오', averageRating: 4.2, totalViews: 650000 }
    ],
    predictionMetrics: {
      expectedGrowth: 15.2,
      retentionRate: 85.7,
      churnRate: 14.3
    }
  };
};

const WebtoonAnalysisSection: React.FC<WebtoonAnalysisSectionProps> = ({ webtoon }) => {
  // 테스트를 위해 더미 데이터 사용
  const dummyData = generateDummyData();
  const analysisData = webtoon.analysisData ? transformToChartData(webtoon.analysisData) : transformToChartData(dummyData);
  const hasData = webtoon.analysisData !== null;
  const hasEnoughData = hasData && analysisData.readerDemographics.ageGroups.length > 0;

  return (
    <section className={styles.section}>
      {!hasEnoughData && (
        <div className={styles.dataInsufficientOverlay}>
          <div className={styles.overlayContent}>
            <p>분석 데이터 표본이 부족합니다</p>
          </div>
        </div>
      )}
      <h2 className={styles.sectionTitle}>웹툰 분석</h2>
      
      {/* 기본 통계 */}
      <div className={styles.statsGrid}>
        <div className={`${styles.statCard} ${!hasData ? styles.disabled : ''}`}>
          <h3>총 조회수</h3>
          <p className={styles.statValue}>{analysisData?.totalViews.toLocaleString()}</p>
        </div>
        <div className={`${styles.statCard} ${!hasData ? styles.disabled : ''}`}>
          <h3>구독자 수</h3>
          <p className={styles.statValue}>{analysisData?.totalSubscribers.toLocaleString()}</p>
        </div>
        <div className={`${styles.statCard} ${!hasData ? styles.disabled : ''}`}>
          <h3>평균 독서 시간</h3>
          <p className={styles.statValue}>{analysisData?.averageViewTime}분</p>
        </div>
        <div className={`${styles.statCard} ${!hasData ? styles.disabled : ''}`}>
          <h3>완독률</h3>
          <p className={styles.statValue}>{analysisData?.completionRate}%</p>
        </div>
      </div>

      {/* 독자 통계 */}
      <div className={styles.chartContainer}>
        <h3>독자 통계</h3>
        <div className={styles.chartGrid}>
          <div className={`${styles.chart} ${!hasData ? styles.disabled : ''}`}>
            <h4>연령대 분포</h4>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={analysisData?.readerDemographics.ageGroups}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="age" tick={{ fontSize: 12 }} />
                <YAxis tick={{ fontSize: 12 }} />
                <Tooltip 
                  contentStyle={{ 
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    border: '1px solid #e9ecef',
                    borderRadius: '8px',
                    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.05)'
                  }}
                />
                <Bar dataKey="percentage" radius={[4, 4, 0, 0]}>
                  {analysisData?.readerDemographics.ageGroups.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={hasData ? entry.color : "#ccc"} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
            {!hasData && <div className={styles.noDataOverlay}>분석 데이터가 없습니다</div>}
          </div>
          <div className={`${styles.chart} ${!hasData ? styles.disabled : ''}`}>
            <h4>성별 분포</h4>
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={analysisData?.readerDemographics.genderDistribution}
                  dataKey="percentage"
                  nameKey="gender"
                  cx="50%"
                  cy="50%"
                  outerRadius={80}
                  label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                >
                  {analysisData?.readerDemographics.genderDistribution.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={hasData ? entry.color : "#ccc"} />
                  ))}
                </Pie>
                <Tooltip 
                  contentStyle={{ 
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    border: '1px solid #e9ecef',
                    borderRadius: '8px',
                    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.05)'
                  }}
                />
              </PieChart>
            </ResponsiveContainer>
            {!hasData && <div className={styles.noDataOverlay}>분석 데이터가 없습니다</div>}
          </div>
        </div>
      </div>

      {/* 성장 지표 */}
      <div className={styles.chartContainer}>
        <h3>성장 지표</h3>
        <div className={`${styles.chart} ${!hasData ? styles.disabled : ''}`}>
          <h4>일일 조회수 추이</h4>
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={analysisData?.growthMetrics.dailyViews}>
              <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
              <XAxis dataKey="date" tick={{ fontSize: 12 }} />
              <YAxis tick={{ fontSize: 12 }} />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: 'rgba(255, 255, 255, 0.95)',
                  border: '1px solid #e9ecef',
                  borderRadius: '8px',
                  boxShadow: '0 2px 4px rgba(0, 0, 0, 0.05)'
                }}
              />
              <Line 
                type="monotone" 
                dataKey="count" 
                stroke={hasData ? "#4ECDC4" : "#ccc"}
                strokeWidth={2}
                dot={{ r: 4 }}
                activeDot={{ r: 6 }}
                name="일일 조회수" 
              />
            </LineChart>
          </ResponsiveContainer>
          {!hasData && <div className={styles.noDataOverlay}>분석 데이터가 없습니다</div>}
        </div>
      </div>

      {/* 콘텐츠 분석 */}
      <div className={styles.chartContainer}>
        <h3>콘텐츠 분석</h3>
        <div className={styles.chartGrid}>
          <div className={`${styles.chart} ${!hasData ? styles.disabled : ''}`}>
            <h4>장르 분포</h4>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={analysisData?.contentAnalysis.genreDistribution}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="genre" tick={{ fontSize: 12 }} />
                <YAxis tick={{ fontSize: 12 }} />
                <Tooltip 
                  contentStyle={{ 
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    border: '1px solid #e9ecef',
                    borderRadius: '8px',
                    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.05)'
                  }}
                />
                <Bar dataKey="percentage" radius={[4, 4, 0, 0]}>
                  {analysisData?.contentAnalysis.genreDistribution.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={hasData ? entry.color : "#ccc"} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
            {!hasData && <div className={styles.noDataOverlay}>분석 데이터가 없습니다</div>}
          </div>
          <div className={`${styles.chart} ${!hasData ? styles.disabled : ''}`}>
            <h4>인기 캐릭터</h4>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={analysisData?.contentAnalysis.characterPopularity}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="character" tick={{ fontSize: 12 }} />
                <YAxis tick={{ fontSize: 12 }} />
                <Tooltip 
                  contentStyle={{ 
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    border: '1px solid #e9ecef',
                    borderRadius: '8px',
                    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.05)'
                  }}
                />
                <Bar dataKey="popularity" radius={[4, 4, 0, 0]}>
                  {analysisData?.contentAnalysis.characterPopularity.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={hasData ? entry.color : "#ccc"} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
            {!hasData && <div className={styles.noDataOverlay}>분석 데이터가 없습니다</div>}
          </div>
        </div>
      </div>

      {/* 예측 분석 */}
      <div className={styles.predictionContainer}>
        <h3>예측 분석</h3>
        <div className={styles.predictionGrid}>
          <div className={`${styles.predictionCard} ${!hasData ? styles.disabled : ''}`}>
            <h4>예상 성장률</h4>
            <p className={styles.predictionValue}>{analysisData?.predictionMetrics.expectedGrowth}%</p>
          </div>
          <div className={`${styles.predictionCard} ${!hasData ? styles.disabled : ''}`}>
            <h4>유지율</h4>
            <p className={styles.predictionValue}>{analysisData?.predictionMetrics.retentionRate}%</p>
          </div>
          <div className={`${styles.predictionCard} ${!hasData ? styles.disabled : ''}`}>
            <h4>이탈률</h4>
            <p className={styles.predictionValue}>{analysisData?.predictionMetrics.churnRate}%</p>
          </div>
        </div>
      </div>
    </section>
  );
};

export default WebtoonAnalysisSection;
