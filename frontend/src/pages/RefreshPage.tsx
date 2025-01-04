import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../services/AuthService';
import { getStoredTokens } from '../services/AuthToken';
import styles from './RefreshPage.module.css';

const RefreshPage: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const refreshSession = async () => {
      try {
        const tokens = getStoredTokens();
        if (!tokens?.refreshToken) {
          navigate('/signin');
          return;
        }

        const response = await AuthService.refreshToken(tokens.refreshToken);
        if (response.success) {
          navigate('/');
        } else {
          navigate('/signin');
        }
      } catch (error) {
        navigate('/signin');
      }
    };

    refreshSession();
  }, [navigate]);

  return (
    <div className={styles.refreshPage}>
      <div className={styles.loading}>세션 갱신 중...</div>
    </div>
  );
};

export default RefreshPage; 