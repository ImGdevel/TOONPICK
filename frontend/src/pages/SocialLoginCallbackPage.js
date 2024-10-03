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
      const result = await AuthService.handleSocialLoginCallback(login);
      console.log(result.success);
      if (result.success) {
        console.log("direct home");
        navigate('/');
      } else {
        console.error(result.message);
        navigate('/login');
      }
    };

    handleCallback();
  }, [login, navigate]);

  return <div>로그인 중...</div>;
};

export default SocialLoginCallbackPage;
