import React from 'react';
import styles from './style.module.css';
import { SerializationStatus } from '@models/enum';

interface StatusBadgeProps {
  status?: SerializationStatus;
  text?: string;
  size?: 'small' | 'medium' | 'large';
}

const StatusBadge: React.FC<StatusBadgeProps> = ({ status, text, size = 'medium' }) => {
  const getStatusText = (status?: SerializationStatus): string => {
    if (!status) return '';
    
    switch (status) {
      case 'ONGOING':
        return '연재중';
      case 'COMPLETED':
        return '완결';
      case 'HIATUS':
        return '휴재';
      default:
        return '';
    }
  };

  return (
    <span className={`${styles.badge} ${status ? styles[status] : ''} ${styles[size]}`}>
      {text || getStatusText(status)}
    </span>
  );
};

export default StatusBadge; 