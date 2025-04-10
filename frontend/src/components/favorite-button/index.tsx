import React from 'react';
import styles from './style.module.css';

interface FavoriteButtonProps {
  isFavorited: boolean;
  onClick: () => void;
}

const FavoriteButton: React.FC<FavoriteButtonProps> = ({ isFavorited, onClick }) => {
  return (
    <button 
      className={`${styles.favoriteButton} ${isFavorited ? styles.active : ''}`}
      onClick={onClick}
      aria-label={isFavorited ? "즐겨찾기 해제" : "즐겨찾기 추가"}
    >
      ★
    </button>
  );
};

export default FavoriteButton; 