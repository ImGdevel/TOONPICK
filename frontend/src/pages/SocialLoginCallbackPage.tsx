import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { SocialLoginCallbackProps } from '../types/auth-page';
import AuthService from '../services/AuthService';
import styles from './SocialLoginCallbackPage.module.css';

const SocialLoginCallbackPage: React.FC = () => {
  const navigate = useNavigate();
  const { provider } = useParams<{ provider: string }>();
  const [error, setError] = useState<string>('');

  useEffect(() => {
    const handleSocialLogin = async () => {
      try {
        // URL에서 인증 코드 추출
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');

        if (!code) {
          throw new Error('인증 코드가 없습니다.');
        }

        // 소셜 로그인 처리
        const response = await AuthService.handleSocialLoginCallback(() => navigate('/'));
        
        if (response.success) {
          navigate('/');
        } else {
          throw new Error('로그인에 실패했습니다.');
        }
      } catch (err) {
        setError('소셜 로그인에 실패했습니다. 다시 시도해주세요.');
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