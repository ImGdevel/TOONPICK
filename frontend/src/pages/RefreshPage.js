// src/pages/RefreshPage.js
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthService } from '../services/AuthService';

const RefreshPage = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const refreshAccessToken = async () => {
      const result = await AuthService.reissueAccessToken();
      if (result.success) {
        navigate('/'); // 리프레시 성공 시 홈으로 이동
      } else {
        navigate('/login'); // 실패 시 로그인 페이지로 이동
      }
    };

    refreshAccessToken();
  }, [navigate]);

  return <div>Refreshing...</div>;
};

export default RefreshPage;
