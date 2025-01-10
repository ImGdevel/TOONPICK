import React, { useContext } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from '@components/Header';
import { AuthContext, AuthProvider } from '@contexts/AuthContext';

import HomePage from '@pages/HomePage';
import SignInPage from '@pages/auth/SignInPage';
import SignUpPage from '@pages/auth/SignUpPage';
import WebtoonDetailPage from '@pages/WebtoonDetailPage';
import MyPage from '@pages/MyPage';
import SocialLoginCallbackPage from '@pages/auth/SocialLoginCallbackPage';
import NewWebtoonsPage from '@pages/NewWebtoonsPage';
import OngoingWebtoonsPage from '@pages/OngoingWebtoonsPage';
import CompletedWebtoonsPage from '@pages/CompletedWebtoonsPage';
import ErrorPage from '@pages/ErrorPage/ErrorPage';
import LoginRequiredModal from './components/LoginRequiredModal';

const App: React.FC = () => {

  const { isLoginModalVisible, hideLoginModal } = useContext(AuthContext);

  console.log(isLoginModalVisible);

  const handleLoginRedirect = () => {
    hideLoginModal();
    window.location.href = '/login';
  };

  return (
    <AuthProvider>
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
            <Route path="/mypage" element={<MyPage />} />
            <Route path="/refresh" element={<SocialLoginCallbackPage />} />
            <Route path="*" element={<ErrorPage />} />
          </Routes>
        </main>
        {/* 모달 창 렌더링 */}
        {isLoginModalVisible && (
        <LoginRequiredModal
            onClose={hideLoginModal}
            onLogin={handleLoginRedirect}
          />
        )}  
      </Router>
    </AuthProvider>
  );
};

export default App; 