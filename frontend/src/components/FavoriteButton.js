import React from 'react';
import styles from './FavoriteButton.module.css';

const FavoriteButton = ({ isFavorite, onClick }) => {
  return (
    <button 
      className={styles['heart-button']} 
      onClick={onClick}
      aria-label={isFavorite ? '좋아요 취소' : '좋아요'}
    >
      {isFavorite ? '❤' : '♡'}
    </button>
  );
};

export default FavoriteButton; 