export interface LoginFormData {
  email: string;
  password: string;
}

export interface SignInFormData extends LoginFormData {
  rememberMe: boolean;
}

export interface SignUpFormData extends LoginFormData {
  username: string;
  confirmPassword: string;
  agreeToTerms: boolean;
}

export interface SocialLoginCallbackProps {
  provider: 'google' | 'kakao' | 'naver';
} 