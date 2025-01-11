import React from 'react';
import styles from './BookmarkButton.module.css';

interface BookmarkButtonProps {
  isBookmarked: boolean;
  onClick: () => void;
}

const BookmarkButton: React.FC<BookmarkButtonProps> = ({ isBookmarked, onClick }) => {
  return (
    <button 
      className={`${styles.bookmarkButton} ${isBookmarked ? styles.active : ''}`}
      onClick={onClick}
      aria-label={isBookmarked ? "북마크 해제" : "북마크 추가"}
    >
      🔖
    </button>
  );
};

export default BookmarkButton; 