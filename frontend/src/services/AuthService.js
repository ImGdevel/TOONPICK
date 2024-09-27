// src/services/AuthService.js
import AuthToken from './AuthToken';
import api from './ApiService';

export const AuthService = {
  login: async (username, password, loginCallback) => {
    try {
      const response = await api.post('/login', { username, password }, { authRequired: false });
      const accessToken = AuthToken.extractAccessTokenFromHeader(response.headers);

      if (accessToken) {
        AuthToken.setAccessToken(accessToken);
        if (loginCallback) loginCallback();
        console.log("login")
        return { success: true };
      } else {
        return { success: false, message: 'Login failed' };
      }
    } catch (error) {
      return { success: false, message: error.message };
    }
  },

  signup: async (username, password, confirmPassword) => {
    if (password !== confirmPassword) {
      return { success: false, message: 'Passwords do not match.' };
    }
    try {
      const response = await api.post('/join', { username, password }, { authRequired: false });
      console.log(response);
      return { success: true };
    } catch (error) {
      return { success: false, message: error.message };
    }
  },

  logout: async () => {
    try {
      await api.post('/logout', null, { authRequired: true });
      AuthToken.clearAccessToken();
      return { success: true };
    } catch (error) {
      return { success: false, message: error.message };
    }
  },
  
  isLoggedIn: () => {
    const accessToken = AuthToken.getAccessToken();
    if (!accessToken) {
      console.log("AccessToken is not available.");
      return false;
    }

    const isValid = !AuthToken.isAccessTokenExpired(accessToken);
    console.log("Login status:", isValid);
    return isValid;
  },

  socialLogin: (provider) => {
    const loginUrl = `http://localhost:8080/oauth2/authorization/${provider}`;
    window.location.href = loginUrl;
  },

  handleSocialLoginCallback: (loginCallback) => {
    const urlParams = new URLSearchParams(window.location.search);
    const accessToken = urlParams.get('accessToken');
    if (accessToken) {
      localStorage.setItem('accessToken', accessToken);
      if (loginCallback) loginCallback();
      return { success: true };
    } else {
      return { success: false, message: 'No access token found in URL' };
    }
  },

};

export default AuthService;