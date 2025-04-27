import React from 'react';
import { useRouteError } from 'react-router-dom';
import styles from './style.module.css';

interface RouteError {
  statusText?: string;
  message?: string;
  status?: number;
}

const ErrorPage: React.FC = () => {
  const error = useRouteError() as RouteError;

  return (
    <div className={styles.errorPage}>
      <h1>오류가 발생했습니다</h1>
      <p>죄송합니다. 예상치 못한 오류가 발생했습니다.</p>
      <p className={styles.errorDetails}>
        {error.status && <span>상태 코드: {error.status}</span>}
        {error.statusText && <span>오류 내용: {error.statusText}</span>}
        {error.message && <span>메시지: {error.message}</span>}
      </p>
      <button 
        onClick={() => window.location.href = '/'}
        className={styles.homeButton}
      >
        홈으로 돌아가기
      </button>
    </div>
  );
};

export default ErrorPage; 