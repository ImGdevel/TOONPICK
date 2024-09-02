// src/components/ErrorPage.js
import React from 'react';
import { useNavigate } from 'react-router-dom';

const ErrorPage = () => {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate('/');
  };

  return (
    <div style={{ textAlign: 'center', padding: '20px' }}>
      <h1>오류가 발생했습니다</h1>
      <p>요청을 처리하는 중 문제가 발생했습니다. 다시 시도해 주세요.</p>
      <button onClick={handleGoHome}>홈으로 이동</button>
    </div>
  );
};

export default ErrorPage;
