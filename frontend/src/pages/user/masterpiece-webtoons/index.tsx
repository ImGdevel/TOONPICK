import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '@contexts/auth-context';
import { Routes } from '@constants/routes';
import WebtoonGrid from '@components/webtoon-grid';
import Spinner from '@components/spinner';
import styles from './style.module.css';

const MasterpieceWebtoonsPage: React.FC = () => {
  const { state } = useContext(AuthContext);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!state.isLoggedIn) {
      navigate(Routes.LOGIN);
      return;
    }

    const fetchMasterpieceWebtoons = async () => {
      try {
        setIsLoading(true);
        // TODO: API 연동
        setIsLoading(false);
      } catch (error) {
        setError('명작 웹툰을 불러오는데 실패했습니다.');
        setIsLoading(false);
      }
    };

    fetchMasterpieceWebtoons();
  }, [state.isLoggedIn, navigate]);

  if (error) return <div className={styles.error}>{error}</div>;
  if (isLoading) return <Spinner />;

  return (
    <div className={styles.page}>
      <h1 className={styles.title}>내 명작 웹툰</h1>
      <div className={styles.content}>
        {state.memberProfile?.masterpieceWebtoons && state.memberProfile.masterpieceWebtoons.length > 0 ? (
          <WebtoonGrid
            webtoons={state.memberProfile.masterpieceWebtoons}
            loading={isLoading}
            onWebtoonClick={(webtoon) => navigate(`/webtoon/${webtoon.id}`)}
          />
        ) : (
          <div className={styles.emptyMessage}>아직 명작으로 등록한 웹툰이 없습니다.</div>
        )}
      </div>
    </div>
  );
};

export default MasterpieceWebtoonsPage;
