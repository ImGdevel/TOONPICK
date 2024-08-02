// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import Home from './pages/Home';
import Recommendation from './pages/Recommendation';
import WebtoonDetailPage from './pages/WebtoonDetailPage';

const App = () => (
  <Router>
    <Header />
    <main>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/recommendation" element={<Recommendation />} />
        <Route path="/webtoon/:id" element={<WebtoonDetailPage />} />
      </Routes>
    </main>
    <Footer />
  </Router>
);

export default App;
