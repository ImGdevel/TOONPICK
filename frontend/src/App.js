// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import MenuBar from './components/MenuBar';
import Home from './pages/Home';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import Recommendation from './pages/Recommendation';
import WebtoonDetailPage from './pages/WebtoonDetailPage';
import ExplorePage from './pages/ExplorePage';
import MyPage from './pages/MyPage';
import { AuthProvider } from './context/AuthContext';
import SocialLoginCallbackPage from './pages/SocialLoginCallbackPage';
import ErrorPage from './components/ErrorPage';
import NewWebtoonsPage from './pages/NewWebtoonsPage';
import OngoingWebtoonsPage from './pages/OngoingWebtoonsPage';
import CompletedWebtoonsPage from './pages/CompletedWebtoonsPage';

const App = () => (
  <AuthProvider>
    <Router>
      <Header />
      <MenuBar />
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/recommendation" element={<Recommendation />} />
          <Route path="/webtoon/new" element={<NewWebtoonsPage />} />
          <Route path="/webtoon/ongoing" element={<OngoingWebtoonsPage />} />
          <Route path="/webtoon/completed" element={<CompletedWebtoonsPage />} />
          <Route path="/webtoon/:id" element={<WebtoonDetailPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/explore" element={<ExplorePage />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/refresh" element={<SocialLoginCallbackPage />} />
          <Route path="*" element={<ErrorPage />} />
        </Routes>
      </main>
    </Router>
  </AuthProvider>
);

export default App;
