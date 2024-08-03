// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import Home from './pages/Home';
import LoginPage from './components/LoginPage';
import SignupPage from './components/SignupPage';
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
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
      </Routes>
    </main>
    <Footer />
  </Router>
);

export default App;
