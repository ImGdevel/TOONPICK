import React, { createContext, useState, useEffect, ReactNode } from 'react';
import AuthService from '@services/AuthService';

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
    console.log("Initial isLoggedIn:", isLoggedIn);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const login = (): void => {
    setIsLoggedIn(true);
    console.log("login isLoggedIn:", isLoggedIn);
  };

  const logout = (): void => {
    AuthService.logout();
    console.log("logout");
    setIsLoggedIn(false);
    console.log("logout isLoggedIn:", isLoggedIn);
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}; 