import React from 'react';
import styles from './style.module.css';

interface LoginRequiredModalProps {
  onClose: () => void;
  onLogin: () => void;
}

const LoginRequiredModal: React.FC<LoginRequiredModalProps> = ({ onClose, onLogin }) => {
  return (
    <div className={styles.modal}>
      <div className={styles.modalContent}>
        <h2>로그인이 필요합니다</h2>
        <p>이 작업을 진행하려면 로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?</p>
        <div className={styles.actions}>
          <button onClick={onLogin} className={styles.loginButton}>
            로그인
          </button>
          <button onClick={onClose} className={styles.cancelButton}>
            취소
          </button>
        </div>
      </div>
    </div>
  );
};

export default LoginRequiredModal;
