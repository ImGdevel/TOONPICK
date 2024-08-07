import React from 'react';
import { useRouter } from 'next/router';
import styles from '../styles/SectionTitle.module.css';

const SectionTitle = ({ title }) => {
  const router = useRouter();

  const handleLogin = () => {
    router.push('/login');
  };

  const handleSignup = () => {
    router.push('/signup');
  };

  return (
    <div className={styles.sectionTitle}>
      <h1>{title}</h1>
      <div className={styles.authButtons}>
        <button onClick={handleLogin}>로그인</button>
        <button onClick={handleSignup}>회원가입</button>
      </div>
    </div>
  );
};

export default SectionTitle;
