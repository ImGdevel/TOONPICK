// src/pages/SocialLoginCallbackPage.js
import React, { useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthService } from '../services/AuthService';
import { AuthContext } from '../context/AuthContext';

const SocialLoginCallbackPage = () => {
  const navigate = useNavigate();
  const { login } = useContext(AuthContext);

  useEffect(() => {
    const handleCallback = async () => {
      const result = AuthService.handleSocialLoginCallback(login);
      console.log(result.success);
      if (result.success) {
        navigate('/'); // 로그인 성공 시 홈으로 이동
      } else {
        console.error(result.message);
        navigate('/login'); // 실패 시 로그인 페이지로 이동
      }
    };

    handleCallback();
  }, [login, navigate]);

  return <div>로그인 중...</div>;
};

export default SocialLoginCallbackPage;
