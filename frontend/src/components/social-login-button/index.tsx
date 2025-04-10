import React from 'react';
import styles from './style.module.css';
import { FaGoogle } from 'react-icons/fa';
import { SiNaver } from 'react-icons/si';
import { RiKakaoTalkFill } from 'react-icons/ri';

interface SocialLoginButtonProps {
  provider: 'google' | 'kakao' | 'naver';
  onClick: () => void;
  type?: 'button' | 'submit';
}

const SocialLoginButton: React.FC<SocialLoginButtonProps> = ({ provider, onClick, type = 'button' }) => {
  const getIcon = () => {
    switch (provider) {
      case 'google':
        return <FaGoogle />;
      case 'kakao':
        return <RiKakaoTalkFill />;
      case 'naver':
        return <SiNaver />;
      default:
        return null;
    }
  };

  return (
    <button className={styles.socialButton} onClick={onClick} type={type}>
      {getIcon()}
      {provider === 'google' && ' Google로 로그인'}
      {provider === 'kakao' && ' 카카오로 로그인'}
      {provider === 'naver' && ' 네이버로 로그인'}
    </button>
  );
};

export default SocialLoginButton; 