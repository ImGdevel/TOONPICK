// src/components/SectionTitle.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './SectionTitle.css';

const SectionTitle = ({ title }) => {
  const navigate = useNavigate();

  const handleLogin = () => {
    navigate('/login');
  };

  const handleSignup = () => {
    navigate('/signup');
  };

  return (
    <div className="section-title">
      <h1>{title}</h1>
      <div className="auth-buttons">
        <button onClick={handleLogin}>로그인</button>
        <button onClick={handleSignup}>회원가입</button>
      </div>
    </div>
  );
};

export default SectionTitle;
