import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '@contexts/auth-context';
import { Routes } from '@constants/routes';
import styles from './style.module.css';
import AuthService from '@services/auth-service';
import SocialLoginButton from '@components/social-login-button';


const SignInPage: React.FC = () => {
  const navigate = useNavigate();
  const { state, login } = useContext(AuthContext);
  const [formData, setFormData] = useState({ username: '', password: '', rememberMe: false });
  const [error, setError] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);

  // 로그인 상태에 따라 홈으로 리다이렉트
  useEffect(() => {
    if (state.isLoggedIn) {
      navigate(Routes.HOME);
    }
  }, [state.isLoggedIn, navigate]);

  // 입력 필드 변경 처리
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  };

  // 로그인 폼 제출 처리
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    if (!formData.username || !formData.password) {
      setError('아이디와 비밀번호를 입력해주세요.');
      setIsLoading(false);
      return;
    }

    try {
      await login(formData.username, formData.password); // 로그인 요청
      if (state.isLoggedIn) {
        navigate(Routes.TUTORIAL); // 로그인 성공 시 튜토리얼 페이지로 이동
      }
    } catch (err: any) {
      setError(err.message || '로그인에 실패했습니다. 다시 시도해주세요.');
    } finally {
      setIsLoading(false);
    }
  };

  // 소셜 로그인 처리
  const handleSocialLogin = (provider: 'google' | 'kakao' | 'naver') => {
    try {
      AuthService.socialLogin(provider); // 소셜 로그인 요청
    } catch (err) {
      setError('소셜 로그인에 실패했습니다. 다시 시도해주세요.');
    }
  };
  

  return (
    <div className={styles.signInPage}>
      <form onSubmit={handleSubmit} className={styles.form}>
        <h1>TOONPICK</h1>
        <h3>웹툰 리뷰 플랫폼</h3>

        {error && <div className={styles.error}>{error}</div>}
        {state.error && <div className={styles.error}>{state.error}</div>}

        <div className={styles.formGroup}>
          <input
            type="text"
            name="username"
            placeholder="아이디"
            value={formData.username}
            onChange={handleChange}
          />
        </div>

        <div className={styles.formGroup}>
          <input
            type="password"
            name="password"
            placeholder="비밀번호"
            value={formData.password}
            onChange={handleChange}
          />
        </div>

        <div className={styles.rememberMe}>
          <input
            type="checkbox"
            id="rememberMe"
            name="rememberMe"
            checked={formData.rememberMe}
            onChange={handleChange}
          />
          <label htmlFor="rememberMe">로그인 상태 유지</label>
        </div>

        <button type="submit" className={styles.submitButton} disabled={isLoading}>
          {isLoading ? '로딩 중...' : '로그인'}
        </button>

        <div className={styles.divider}>
          <span>또는</span>
        </div>

        <div className={styles.socialLogin}>
          <SocialLoginButton provider="google" onClick={() => handleSocialLogin('google')} type="button" />
          <SocialLoginButton provider="kakao" onClick={() => handleSocialLogin('kakao')} type="button" />
          <SocialLoginButton provider="naver" onClick={() => handleSocialLogin('naver')} type="button" />
        </div>

        <div className={styles.links}>
          <Link to="/signup">회원가입</Link>
          <Link to="/forgot-password">비밀번호 찾기</Link>
        </div>
      </form>
    </div>
  );
};

export default SignInPage;
