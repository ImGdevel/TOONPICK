import React from 'react';
import styles from './WebtoonTag.module.css';

interface WebtoonTagProps {
  text: string;
  color?: 'default' | 'primary' | 'secondary';
  onClick?: () => void;
}

const WebtoonTag: React.FC<WebtoonTagProps> = ({
  text,
  color = 'default',
  onClick
}) => {
  return (
    <span 
      className={`${styles.webtoonTag} ${styles[color]}`}
      onClick={onClick}
      style={{ cursor: onClick ? 'pointer' : 'default' }}
    >
      {text}
    </span>
  );
};

export default WebtoonTag; 