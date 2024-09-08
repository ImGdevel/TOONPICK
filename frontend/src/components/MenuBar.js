// src/components/MenuBar.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './MenuBar.module.css'; // Assuming you're using CSS Modules

const MenuBar = () => {
  const navigate = useNavigate();

  const handleNavigation = (path) => {
    navigate(path);
  };



  return (
    <nav className={styles.menuBar}>
      <ul className={styles.menuList}>
        <li onClick={() => handleNavigation('/')} className={styles.menuItem}>홈</li>
        <li onClick={() => handleNavigation('/webtoon/ongoing')} className={styles.menuItem}>연재</li>
        <li onClick={() => handleNavigation('/webtoon/new')} className={styles.menuItem}>신작</li>
        <li onClick={() => handleNavigation('/webtoon/completed')} className={styles.menuItem}>완결</li>
        <li onClick={() => handleNavigation('/recommendation')} className={styles.menuItem}>추천</li>
      </ul>
      <input 
        type="text" 
        placeholder="검색..." 
        className={styles.searchInput}
        onKeyDown={(e) => {
          if (e.key === 'Enter') {
            handleNavigation(`/search?query=${e.target.value}`);
          }
        }}
      />
    </nav>
  );
};

export default MenuBar;
