// src/services/UserService.js
import api from './ApiService';

// 사용자 프로필 정보 가져오기
export const getUserProfile = async () => {
  try {
    const response = await api.get('/api/user/profile', { authRequired: true });
    console.log("success!")
    return { success: true, data: response.data };
  } catch (error) {
    console.log("request fail!")
    console.error('Error fetching user profile:', error);
    return { success: false, message: error.message };
  }
};
