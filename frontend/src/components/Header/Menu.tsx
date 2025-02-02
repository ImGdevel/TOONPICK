import React from 'react';
import styles from './Header.module.css';

interface MenuProps {
  navigate: (path: string) => void;
}

const Menu: React.FC<MenuProps> = ({ navigate }) => {
  return (
    <nav className={styles.menuBar}>
      <ul className={styles.menuList}>
        <li onClick={() => navigate('/webtoon/ongoing')} className={styles.menuItem}>연재</li>
        <li onClick={() => navigate('/webtoon/new')} className={styles.menuItem}>신작</li>
        <li onClick={() => navigate('/webtoon/completed')} className={styles.menuItem}>완결</li>
      </ul>
    </nav>
  );
};

export default Menu; 