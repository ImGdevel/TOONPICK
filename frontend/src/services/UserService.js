// src/services/UserService.js

// 사용자 프로필 정보 가져오기
export const getUserProfile = async () => {
    const token = localStorage.getItem('accessToken'); 
    try {
      const response = await fetch('http://localhost:8080/api/user/profile', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
      });
      if (!response.ok) {
        throw new Error('Failed to fetch user profile');
      }
      const userProfile = await response.json();
      return { success: true, data: userProfile };
    } catch (error) {
      console.error('Error fetching user profile:', error);
      return { success: false, message: error.message };
    }
  };
  