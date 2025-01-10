import React, { createContext, useState, useEffect, ReactNode } from 'react';
import AuthService from '@services/AuthService';

interface AuthContextType {
  isLoggedIn: boolean;
  login: () => void;
  logout: () => void;
  showLoginModal: () => void;
  hideLoginModal: () => void;
  isLoginModalVisible: boolean;
}

export const AuthContext = createContext<AuthContextType>({
  isLoggedIn: false,
  login: () => {},
  logout: () => {},
  showLoginModal: () => {},
  hideLoginModal: () => {},
  isLoginModalVisible: false,
});

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(AuthService.isLoggedIn());
  const [isLoginModalVisible, setIsLoginModalVisible] = useState<boolean>(false);

  useEffect(() => {
    
    setIsLoggedIn(AuthService.isLoggedIn());
    console.log(isLoginModalVisible);
    console.log(AuthService.isLoggedIn());
  }, []);

  const login = (): void => setIsLoggedIn(true);
  const logout = (): void => {
    AuthService.logout();
    setIsLoggedIn(false);
  };

  useEffect(() => {
    setIsLoginModalVisible(true); // 모달 강제 표시
  }, []);
  

  const showLoginModal = (): void => setIsLoginModalVisible(true);
  const hideLoginModal = (): void => setIsLoginModalVisible(false);

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout, showLoginModal, hideLoginModal, isLoginModalVisible }}>
      {children}
    </AuthContext.Provider>
  );
};
