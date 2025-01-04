import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import HomePage from './pages/HomePage';
import SignInPage from './pages/SignInPage';
import SignUpPage from './pages/SignUpPage';
import WebtoonDetailPage from './pages/WebtoonDetailPage';
import ExplorePage from './pages/ExplorePage';
import MyPage from './pages/MyPage';
import { AuthProvider } from './context/AuthContext';
import SocialLoginCallbackPage from './pages/SocialLoginCallbackPage';
import ErrorPage from './components/ErrorPage';
import NewWebtoonsPage from './pages/NewWebtoonsPage';
import OngoingWebtoonsPage from './pages/OngoingWebtoonsPage';
import CompletedWebtoonsPage from './pages/CompletedWebtoonsPage';

const App: React.FC = () => {
  return (
    <AuthProvider>
      <Router>
        <Header />
        <main>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/webtoon/new" element={<NewWebtoonsPage />} />
            <Route path="/webtoon/ongoing" element={<OngoingWebtoonsPage />} />
            <Route path="/webtoon/completed" element={<CompletedWebtoonsPage />} />
            <Route path="/webtoon/:id" element={<WebtoonDetailPage />} />
            <Route path="/login" element={<SignInPage />} />
            <Route path="/signin" element={<SignInPage />} />
            <Route path="/signup" element={<SignUpPage />} />
            <Route path="/explore" element={<ExplorePage />} />
            <Route path="/mypage" element={<MyPage />} />
            <Route path="/refresh" element={<SocialLoginCallbackPage />} />
            <Route path="*" element={<ErrorPage />} />
          </Routes>
        </main>
      </Router>
    </AuthProvider>
  );
};

export default App; 