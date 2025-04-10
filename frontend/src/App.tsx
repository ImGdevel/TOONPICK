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
import { Routes as RoutePaths } from '@constants/routes';

const App: React.FC = () => {
  return (
    <AuthProvider>
      <ModalProvider>
      <Router>
        <Header />
        <main style={{ maxWidth: '1200px', width: '100%', margin: '0 auto', padding: '20px' }}>
          <Routes>
            <Route path={RoutePaths.HOME} element={<HomePage />} />

            {/* 웹툰 관련 페이지 */}
            <Route path={RoutePaths.WEBTOON_NEW} element={<NewWebtoonsPage />} />
            <Route path={RoutePaths.WEBTOON_ONGOING} element={<OngoingWebtoonsPage />} />
            <Route path={RoutePaths.WEBTOON_COMPLETED} element={<CompletedWebtoonsPage />} />
            <Route path="/webtoon/:id" element={<WebtoonDetailPage />} />

            {/* Auth 관련 페이지 */}
            <Route path="/auth/login" element={<SignInPage />} />
            <Route path="/auth/signin" element={<SignInPage />} />
            <Route path="/auth//signup" element={<SignUpPage />} />
            <Route path="/auth//login-success" element={<SocialLoginCallbackPage />} />

            {/* 유저 관련 페이지 */}
            <Route path={RoutePaths.USER_PROFILE} element={<MyProfilePage />} />
            <Route path={RoutePaths.USER_PROFILE_EDIT} element={<ProfileEditPage />} />
            <Route path={RoutePaths.ERROR} element={<ErrorPage />} />
          </Routes>
        </main>
      </Router>
      </ModalProvider>
    </AuthProvider>
  );
};

export default App; 