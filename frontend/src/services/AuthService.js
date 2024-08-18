// src/services/AuthService.js
export const AuthService = {
  login: async (username, password, loginCallback) => {
    try {
      const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
        credentials: 'include',
      });
  
      if (response.ok) {
        const accessToken = response.headers.get('access');
        localStorage.setItem('accessToken', accessToken);
        if (loginCallback) loginCallback(); // 로그인 상태 업데이트
        return { success: true };
      } else {
        const errorData = await response.json();
        return { success: false, message: errorData.message || 'Login failed' };
      }
    } catch (error) {
      return { success: false, message: error.message };
    }
  },
  
    logout: () => {
      localStorage.removeItem('accessToken');
      // 다른 로그아웃 관련 처리 추가 가능
    },
  
    getAccessToken: () => {
      return localStorage.getItem('accessToken');
    },
  
    isLoggedIn: () => {
      return !!localStorage.getItem('accessToken');
    },
  
    reissueAccessToken: async () => {
      try {
        const response = await fetch('http://localhost:8080/reissue', {
          method: 'POST',
          credentials: 'include', // 쿠키 포함
        });
  
        if (response.ok) {
          const newAccessToken = response.headers.get('access');
          localStorage.setItem('accessToken', newAccessToken);
          return { success: true };
        } else {
          return { success: false, message: 'Failed to refresh access token' };
        }
      } catch (error) {
        return { success: false, message: error.message };
      }
    },

    async signup(username, password, confirmPassword) {
      if (password !== confirmPassword) {
        return { success: false, message: "Passwords do not match." };
      }
  
      try {
        const response = await fetch("http://localhost:8080/join", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username,
            password,
          }),
        });
  
        if (response.ok) {
          return { success: true };
        } else {
          const message = await response.text();
          return { success: false, message };
        }
      } catch (error) {
        return { success: false, message: "An error occurred. Please try again." };
      }
    },
  };
  