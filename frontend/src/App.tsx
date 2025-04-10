import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from '@components/header';

import { AuthProvider } from '@contexts/auth-context';
import { ModalProvider } from '@contexts/modal-context';

import HomePage from '@pages/home';
import SignInPage from '@pages/auth/signin';
import SignUpPage from '@pages/auth/signup';
import WebtoonDetailPage from '@pages/webtoon/webtoon-details';
import MyProfilePage from '@pages/user/profile';
import SocialLoginCallbackPage from '@pages/auth/sociallogin-callback-page';
import NewWebtoonsPage from '@pages/webtoon/new-webtoons';
import OngoingWebtoonsPage from '@pages/webtoon/ongoing-webtoons';
import CompletedWebtoonsPage from '@pages/webtoon/completed-webtoons';
import ProfileEditPage from '@pages/user/profile-edit';
import ErrorPage from '@pages/error';

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
            <Route path="/profile/edit" element={<ProfileEditPage />} />
            <Route path="/login-success" element={<SocialLoginCallbackPage />} />
            <Route path="*" element={<ErrorPage />} />
          </Routes>
        </main>
      </Router>
      </ModalProvider>
    </AuthProvider>
  );
};

export default App; 