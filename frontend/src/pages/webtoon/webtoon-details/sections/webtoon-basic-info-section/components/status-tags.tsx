import React from 'react';
import { SerializationStatus } from '@models/webtoon';
import styles from '../style.module.css';

interface StatusTagsProps {
  status: SerializationStatus;
  isAdult: boolean;
}

const StatusTags: React.FC<StatusTagsProps> = ({ status, isAdult }) => {
  const getStatusText = (status: SerializationStatus) => {
    switch (status) {
      case SerializationStatus.ONGOING:
        return '연재중';
      case SerializationStatus.COMPLETED:
        return '완결';
      case SerializationStatus.HIATUS:
        return '휴재';
      default:
        return '';
    }
  };

  return (
    <div className={styles.statusTags}>
      <div className={styles.statusTagContainer}>
        <span className={`${styles.statusTag} ${styles[status.toLowerCase()]}`}>
          {getStatusText(status)}
        </span>
        {isAdult && <span className={styles.adultTag}>성인</span>}
      </div>
    </div>
  );
};

export default StatusTags; 