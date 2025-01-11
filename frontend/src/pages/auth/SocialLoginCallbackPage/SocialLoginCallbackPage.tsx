import React, { useContext, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import styles from './SocialLoginCallbackPage.module.css';
import TokenRefresher from '@services/TokenRefresher';
import { AuthContext } from '@contexts/AuthContext';

const SocialLoginCallbackPage: React.FC = () => {
  const navigate = useNavigate();
  const { provider } = useParams<{ provider: string }>();
  const { login } = useContext(AuthContext);
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const handleSocialLogin = async () => {
      try {
        const accessToken = await TokenRefresher.refreshAccessToken();

        if (accessToken) {
          login();
          navigate('/');
        } else {
          throw new Error('Access Token이 없습니다.');
        }
      } catch (err: any) {
        setError(`소셜 로그인에 실패했습니다. 다시 시도해주세요. (오류: ${err.message || err})`);
        setTimeout(() => navigate('/signin'), 3000);
      } finally {
        setLoading(false);
      }
    };

    handleSocialLogin();
  }, [navigate, provider, login]);

  return (
    <div className={styles.socialLoginCallback}>
      {loading ? (
        <div className={styles.loading}>
          <span>로그인 처리 중...</span>
          {/* 로딩 애니메이션 추가 */}
          <div className={styles.spinner}></div>
        </div>
      ) : error ? (
        <div className={styles.error}>{error}</div>
      ) : null}
    </div>
  );
};

export default SocialLoginCallbackPage;
