import React from 'react';
import styles from './ErrorModal.module.css';

interface ErrorModalProps {
  message: string;
  onClose: () => void;
}

const ErrorModal: React.FC<ErrorModalProps> = ({ message, onClose }) => {
  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <h2>오류 발생</h2>
        <p>{message}</p>
        <button onClick={onClose}>확인</button>
      </div>
    </div>
  );
};

export default ErrorModal; 