import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './style.module.css';
import { Routes } from '@constants/routes';
import BasicInfoForm from './sections/basic-info'
import GenrePreferenceForm from './sections/genre-preference';
import WebtoonRatingForm from './sections/webtoon-rating';

const TutorialPage: React.FC = () => {
  const navigate = useNavigate();
  const [currentStep, setCurrentStep] = useState<number>(1);
  const [basicInfo, setBasicInfo] = useState<{ gender: string; birthYear: number } | null>(null);
  const [selectedGenres, setSelectedGenres] = useState<string[]>([]);

  const handleBasicInfoComplete = (data: { gender: string; birthYear: number }) => {
    setBasicInfo(data);
    setCurrentStep(2);
  };

  const handleGenrePreferenceComplete = (genres: string[]) => {
    setSelectedGenres(genres);
    setCurrentStep(3);
  };

  const handleWebtoonRatingComplete = () => {
    navigate(Routes.HOME);
  };

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        {currentStep === 1 && (
          <div className={styles.step}>
            <h2 className={styles.title}>기본 정보 수집</h2>
            <BasicInfoForm onComplete={handleBasicInfoComplete} />
          </div>
        )}
        
        {currentStep === 2 && (
          <div className={styles.step}>
            <h2 className={styles.title}>웹툰 성향 선택</h2>
            <GenrePreferenceForm onComplete={handleGenrePreferenceComplete} onSkip={() => setCurrentStep(3)} />
          </div>
        )}
        
        {currentStep === 3 && (
          <div className={styles.step}>
            <h2 className={styles.title}>웹툰 평가</h2>
            <WebtoonRatingForm onComplete={handleWebtoonRatingComplete} />
          </div>
        )}
      </div>
    </div>
  );
};

export default TutorialPage; 