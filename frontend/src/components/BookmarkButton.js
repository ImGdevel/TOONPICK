import React from 'react';
import styles from './BookmarkButton.module.css';

const BookmarkButton = ({ isBookmarked, onClick }) => {
  return (
    <button 
      className={styles['bookmark-button']} 
      onClick={onClick}
      aria-label={isBookmarked ? '북마크 취소' : '북마크 추가'}
    >
      북마크
    </button>
  );
};

export default BookmarkButton; 