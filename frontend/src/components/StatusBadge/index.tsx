import React from 'react';
import styles from './StatusBadge.module.css';
import { StatusType } from '../../models/webtoon';

interface StatusBadgeProps {
  status?: StatusType;
  text?: string;
  size?: 'small' | 'medium' | 'large';
}

const StatusBadge: React.FC<StatusBadgeProps> = ({ status, text, size = 'medium' }) => {
  const getStatusText = (status?: StatusType): string => {
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