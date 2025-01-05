import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import styles from './SocialLoginCallbackPage.module.css';
import AuthToken from '@/services/AuthToken';

const SocialLoginCallbackPage: React.FC = () => {
  const navigate = useNavigate();
  const { provider } = useParams<{ provider: string }>();
  const [error, setError] = useState<string>('');

  useEffect(() => {
    const handleSocialLogin = async () => {
      try {
        const accessToken = await AuthToken.refreshAccessToken(); 

        if (accessToken) {
          navigate('/');
        } else {
          throw new Error('Access Token이 없습니다.');
        }
      } catch (err) {
        setError('소셜 로그인에 실패했습니다. 다시 시도해주세요.' + err);
        setTimeout(() => navigate('/signin'), 3000);
      }
    };

    handleSocialLogin();
  }, [navigate, provider]);

  return (
    <div className={styles.socialLoginCallback}>
      {error ? (
        <div className={styles.error}>{error}</div>
      ) : (
        <div className={styles.loading}>로그인 처리 중...</div>
      )}
    </div>
  );
};

export default SocialLoginCallbackPage; 