import React, { createContext, useState, useEffect, ReactNode } from 'react';
import AuthService from '@/services/auth-service';
import MemberService from '@/services/member-service';
import { MemberProfile } from '@models/member';

interface AuthContextType {
  isLoggedIn: boolean;
  login: () => void;
  logout: () => void;
  memberProfile: MemberProfile | null;
}

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthContext = createContext<AuthContextType>({
  isLoggedIn: false,
  login: () => {},
  logout: () => {},
  memberProfile: null,
});

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(AuthService.isLoggedIn());
  const [memberProfile, setMemberProfile] = useState<MemberProfile | null>(null);

  useEffect(() => {
    setIsLoggedIn(AuthService.isLoggedIn());
    if (isLoggedIn) {
      const fetchMemberProfile = async () => {
        const profile = await MemberService.getMemberProfile();
        setMemberProfile(profile.data || null);
      };
      fetchMemberProfile();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isLoggedIn]);

  const login = (): void => {
    setIsLoggedIn(true);
  };

  const logout = (): void => {
    AuthService.logout();
    setIsLoggedIn(false);
    setMemberProfile(null);
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout, memberProfile }}>
      {children}
    </AuthContext.Provider>
  );
}; 