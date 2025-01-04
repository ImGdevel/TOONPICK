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
        navigate('/');
      } else {
        navigate('/login'); 
      }
    };

    refreshAccessToken();
  }, [navigate]);

  return <div>Refreshing...</div>;
};

export default RefreshPage;
