// src/components/Header.js
import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => (
  <header>
    <h1>웹툰 추천 사이트</h1>
    <nav>
      <ul>
        <li><Link to="/">Home</Link></li>
        <li><Link to="/recommendation">Recommendations</Link></li>
      </ul>
    </nav>
  </header>
);

export default Header;
