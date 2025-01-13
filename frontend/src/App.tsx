import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from '@components/Header';

import { AuthProvider } from '@contexts/AuthContext';
import { ModalProvider } from '@contexts/ModalContext';

import HomePage from '@pages/HomePage';
import SignInPage from '@pages/auth/SignInPage';
import SignUpPage from '@pages/auth/SignUpPage';
import WebtoonDetailPage from '@pages/WebtoonDetailPage';
import MyProfilePage from '@pages/MyProfilePage';
import SocialLoginCallbackPage from '@pages/auth/SocialLoginCallbackPage';
import NewWebtoonsPage from '@pages/NewWebtoonsPage';
import OngoingWebtoonsPage from '@pages/OngoingWebtoonsPage';
import CompletedWebtoonsPage from '@pages/CompletedWebtoonsPage';
import ErrorPage from '@pages/ErrorPage/ErrorPage';

const App: React.FC = () => {
  return (
    <AuthProvider>
      <ModalProvider>
      <Router>
        <Header />
        <main style={{ maxWidth: '1200px', width: '100%', margin: '0 auto', padding: '20px' }}>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/webtoon/new" element={<NewWebtoonsPage />} />
            <Route path="/webtoon/ongoing" element={<OngoingWebtoonsPage />} />
            <Route path="/webtoon/completed" element={<CompletedWebtoonsPage />} />
            <Route path="/webtoon/:id" element={<WebtoonDetailPage />} />
            <Route path="/login" element={<SignInPage />} />
            <Route path="/signin" element={<SignInPage />} />
            <Route path="/signup" element={<SignUpPage />} />
            <Route path="/mypage" element={<MyProfilePage />} />
            <Route path="/refresh" element={<SocialLoginCallbackPage />} />
            <Route path="*" element={<ErrorPage />} />
          </Routes>
        </main>
      </Router>
      </ModalProvider>
    </AuthProvider>
  );
};

export default App; 