import React, { useState, useRef } from 'react';
import { FiSearch, FiSun, FiMoon, FiBell } from 'react-icons/fi';
import styles from './style.module.css';

interface SearchProps {
  navigate: (path: string) => void;
  isDarkTheme: boolean;
  toggleTheme: () => void;
}

const Search: React.FC<SearchProps> = ({ navigate, isDarkTheme, toggleTheme }) => {
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

      <button onClick={toggleTheme} className={styles.iconButton}>
        {isDarkTheme ? (
          <FiSun className={styles.icon} color="white" size={24} />
        ) : (
          <FiMoon className={styles.icon} color="white" size={24} />
        )}
      </button>

      <button className={styles.iconButton}>
        <FiBell className={styles.icon} color="white" size={24} />
      </button>
    </div>
  );
};

export default Search; 