import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { SignInFormData } from '../types/auth-page';
import AuthService from '../services/AuthService';
import styles from './SignInPage.module.css';

const SignInPage: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<SignInFormData>({
    email: '',
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
      const response = await AuthService.login({
        email: formData.email,
        password: formData.password
      });

      if (response.success) {
        navigate('/');
      }
    } catch (err) {
      setError('로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.');
    }
  };

  return (
    <div className={styles.signInPage}>
      <h1>로그인</h1>
      {error && <div className={styles.error}>{error}</div>}
      
      <form onSubmit={handleSubmit} className={styles.form}>
        <div className={styles.formGroup}>
          <label htmlFor="email">이메일</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className={styles.formGroup}>
          <label htmlFor="password">비밀번호</label>
          <input
            type="password"
            id="password"
            name="password"
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
      </form>

      <div className={styles.socialLogin}>
        <button onClick={() => window.location.href = '/auth/google'}>
          Google로 로그인
        </button>
        <button onClick={() => window.location.href = '/auth/kakao'}>
          Kakao로 로그인
        </button>
        <button onClick={() => window.location.href = '/auth/naver'}>
          Naver로 로그인
        </button>
      </div>

      <div className={styles.links}>
        <Link to="/signup">회원가입</Link>
        <Link to="/forgot-password">비밀번호 찾기</Link>
      </div>
    </div>
  );
};

export default SignInPage; 