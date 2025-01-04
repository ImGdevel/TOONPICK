import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { FaGoogle } from 'react-icons/fa';
import { SiNaver } from 'react-icons/si';
import { RiKakaoTalkFill } from 'react-icons/ri';
import AuthService from '@/services/AuthService';
import styles from './SignInPage.module.css';

interface SignInForm {
  username: string;
  password: string;
  rememberMe: boolean;
}

const SignInPage: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<SignInForm>({
    username: '',
    password: '',
    rememberMe: false
  });
  const [error, setError] = useState<string>('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');

    try {
      const response = await AuthService.login(
        formData.username,
        formData.password,
        formData.rememberMe,
        () => navigate('/')
      );

      if (response.success) {
        navigate('/');
      }
    } catch (err) {
      setError('로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.');
    }
  };

  const handleSocialLogin = (provider: 'google' | 'kakao' | 'naver') => {
    AuthService.socialLogin(provider);
  };

  return (
    <div className={styles.signInPage}>
      <form onSubmit={handleSubmit} className={styles.form}>
        <h1>TOONPICK</h1>
        <h3>웹툰 리뷰 플랫폼</h3>
        
        {error && <div className={styles.error}>{error}</div>}
        
        <div className={styles.formGroup}>
          <input
            type="text"
            name="username"
            placeholder="아이디"
            value={formData.username}
            onChange={handleChange}
            required
          />
        </div>

        <div className={styles.formGroup}>
          <input
            type="password"
            name="password"
            placeholder="비밀번호"
            value={formData.password}
            onChange={handleChange}
            required
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

        <button type="submit" className={styles.submitButton}>
          로그인
        </button>

        <div className={styles.divider}>
          <span>또는</span>
        </div>

        <div className={styles.socialLogin}>
          <button 
            type="button" 
            className={styles.google}
            onClick={() => handleSocialLogin('google')}
          >
            <FaGoogle />
          </button>
          <button 
            type="button"
            className={styles.kakao}
            onClick={() => handleSocialLogin('kakao')}
          >
            <RiKakaoTalkFill />
          </button>
          <button 
            type="button"
            className={styles.naver}
            onClick={() => handleSocialLogin('naver')}
          >
            <SiNaver />
          </button>
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