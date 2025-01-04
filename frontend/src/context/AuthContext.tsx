import React, { createContext, useState, useEffect, ReactNode } from 'react';
import AuthService from '../services/AuthService';

interface AuthContextType {
  isLoggedIn: boolean;
  login: () => void;
  logout: () => void;
}

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthContext = createContext<AuthContextType>({
  isLoggedIn: false,
  login: () => {},
  logout: () => {},
});

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(AuthService.isLoggedIn());

  useEffect(() => {
    setIsLoggedIn(AuthService.isLoggedIn());
  }, []);

  const login = (): void => {
    setIsLoggedIn(true);
  };

  const logout = (): void => {
    console.log("do logout");
    AuthService.logout();
    console.log("logout");
    setIsLoggedIn(false);
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}; 