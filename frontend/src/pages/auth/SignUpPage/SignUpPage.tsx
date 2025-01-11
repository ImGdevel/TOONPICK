import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import AuthService from '@services/AuthService';
import { SignUpFormData } from '@/models/auth-page';
import styles from './SignUpPage.module.css';

const SignUpPage: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<SignUpFormData>({
    email: '',
    password: '',
    confirmPassword: '',
    username: '',
    agreeToTerms: false
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

    if (formData.password !== formData.confirmPassword) {
      setError('비밀번호가 일치하지 않습니다.');
      return;
    }

    if (!formData.agreeToTerms) {
      setError('이용약관에 동의해주세요.');
      return;
    }

    try {
      const response = await AuthService.signup(
        formData.username,
        formData.password,
        formData.email
      );

      if (response.success) {
        navigate('/login');
      }
    } catch (err) {
      setError('회원가입에 실패했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className={styles.signUpPage}>
      <form onSubmit={handleSubmit} className={styles.form}>
        <h1>TOONPICK</h1>
        <h3>회원가입</h3>

        {error && <div className={styles.error}>{error}</div>}
        
        <div className={styles.formGroup}>
          <label htmlFor="email">이메일</label>
          <input
            type="email"
            id="email"
            name="email"
            placeholder="example@email.com"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className={styles.formGroup}>
          <label htmlFor="username">사용자 이름</label>
          <input
            type="text"
            id="username"
            name="username"
            placeholder="사용자 이름을 입력하세요"
            value={formData.username}
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
            placeholder="비밀번호를 입력하세요"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <div className={styles.formGroup}>
          <label htmlFor="confirmPassword">비밀번호 확인</label>
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            placeholder="비밀번호를 다시 입력하세요"
            value={formData.confirmPassword}
            onChange={handleChange}
            required
          />
        </div>

        <div className={styles.terms}>
          <input
            type="checkbox"
            id="agreeToTerms"
            name="agreeToTerms"
            checked={formData.agreeToTerms}
            onChange={handleChange}
          />
          <label htmlFor="agreeToTerms">
            이용약관 및 개인정보 처리방침에 동의합니다
          </label>
        </div>

        <button type="submit" className={styles.submitButton}>
          회원가입
        </button>

        <div className={styles.links}>
          <Link to="/signin">이미 계정이 있으신가요? 로그인</Link>
        </div>
      </form>
    </div>
  );
};

export default SignUpPage; 