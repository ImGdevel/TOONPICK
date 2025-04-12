import React, { useState, useRef } from 'react';
import { FiSearch } from 'react-icons/fi';
import styles from './style.module.css';

interface SearchProps {
  navigate: (path: string) => void;
}

const Search: React.FC<SearchProps> = ({ navigate }) => {
  const [isSearchInputVisible, setSearchInputVisible] = useState<boolean>(false);
  const searchInputRef = useRef<HTMLInputElement>(null);

  const toggleSearchInput = (event: React.MouseEvent): void => {
    event.stopPropagation();
    setSearchInputVisible((prev) => !prev);
  };

  return (
    <div className={styles.searchContainer}>
      {isSearchInputVisible && (
        <input
          ref={searchInputRef}
          type="text"
          placeholder="검색..."
          className={styles.searchInput}
          onKeyDown={(e: React.KeyboardEvent<HTMLInputElement>) => {
            if (e.key === 'Enter') {
              navigate(`/search?query=${e.currentTarget.value}`);
            }
          }}
        />
      )}
      <button onClick={toggleSearchInput} className={styles.iconButton}>
        <FiSearch className={styles.icon} color="white" size={24} />
      </button>
    </div>
  );
};

export default Search; 