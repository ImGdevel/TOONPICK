// src/context/AuthContext.js
import React, { createContext, useState, useEffect } from 'react';
import { AuthService } from '../services/AuthService';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(AuthService.isLoggedIn());

  useEffect(() => {
    setIsLoggedIn(AuthService.isLoggedIn());
  }, []);

  const login = () => {
    setIsLoggedIn(true);
  };

  const logout = () => {
    console.log("do logout")
    AuthService.logout();
    console.log("logout")
    setIsLoggedIn(false);
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
