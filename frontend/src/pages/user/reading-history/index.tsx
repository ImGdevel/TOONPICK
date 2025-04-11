import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '@contexts/auth-context';
import { Routes } from '@constants/routes';
import WebtoonGrid from '@components/webtoon-grid';
import Spinner from '@components/spinner';
import styles from './style.module.css';

const ReadingHistoryPage: React.FC = () => {
  const { isLoggedIn, memberProfile } = useContext(AuthContext);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isLoggedIn) {
      navigate(Routes.LOGIN);
      return;
    }

    const fetchReadingHistory = async () => {
      try {
        setIsLoading(true);
        // TODO: API 연동
        setIsLoading(false);
      } catch (error) {
        setError('감상 기록을 불러오는데 실패했습니다.');
        setIsLoading(false);
      }
    };

    fetchReadingHistory();
  }, [isLoggedIn, navigate]);

  if (error) return <div className={styles.error}>{error}</div>;
  if (isLoading) return <Spinner />;

  return (
    <div className={styles.page}>
      <h1 className={styles.title}>감상 기록</h1>
      <div className={styles.content}>
        {memberProfile?.readingHistory && memberProfile.readingHistory.length > 0 ? (
          <WebtoonGrid
            webtoons={memberProfile.readingHistory.map(h => h.webtoon)}
          />
        ) : (
          <div className={styles.emptyMessage}>아직 감상한 웹툰이 없습니다.</div>
        )}
      </div>
    </div>
  );
};

export default ReadingHistoryPage;
