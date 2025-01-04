import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '@/services/AuthService';
import styles from './RefreshPage.module.css';

const RefreshPage: React.FC = () => {
  const navigate = useNavigate();
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const refreshAccessToken = async (): Promise<void> => {
      try {
        const result = await AuthService.handleSocialLoginCallback();
        if (result.success) {
          navigate('/', { replace: true });
        } else {
          setError(result.message || '인증에 실패했습니다.');
          setTimeout(() => {
            navigate('/login', { replace: true });
          }, 2000);
        }
      } catch (error) {
        setError(error instanceof Error ? error.message : '알 수 없는 오류가 발생했습니다.');
        setTimeout(() => {
          navigate('/login', { replace: true });
        }, 2000);
      }
    };

    refreshAccessToken();
  }, [navigate]);

  if (error) {
    return (
      <div className={styles.container}>
        <div className={styles.errorMessage}>
          <h2>인증 오류</h2>
          <p>{error}</p>
          <p>로그인 페이지로 이동합니다...</p>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <div className={styles.loadingSpinner}></div>
      <p>인증 처리 중입니다...</p>
    </div>
  );
};

export default RefreshPage; 