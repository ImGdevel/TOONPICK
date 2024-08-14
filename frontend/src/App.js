// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import Home from './pages/Home';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import Recommendation from './pages/Recommendation';
import WebtoonDetailPage from './pages/WebtoonDetailPage';
import ExplorePage from './pages/ExplorePage';

const App = () => (
  <Router>
    <Header />
    <main>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/recommendation" element={<Recommendation />} />
        <Route path="/webtoon/:id" element={<WebtoonDetailPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/explore" element={<ExplorePage />} />
        <Route path="/webtoon/:id" element={<WebtoonDetailPage />} />
      </Routes>
    </main>
    <Footer />
  </Router>
);

export default App;
