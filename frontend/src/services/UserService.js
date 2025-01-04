// src/services/UserService.js
import api from './ApiService';

// 사용자 프로필 정보 가져오기
export const getUserProfile = async () => {
  try {
    const response = await api.get('/api/user/profile', { authRequired: true });
    return { success: true, data: response.data };
  } catch (error) {
    console.error('Error fetching user profile:', error);
    return { success: false, message: error.message };
  }
};

// 관심 웹툰 추가
export const addFavoriteWebtoon = async (webtoonId) => {
  try {
    const response = await api.post(`/api/users/favorites/${webtoonId}`, null, { authRequired: true });
    return { success: true };
  } catch (error) {
    console.error('Error adding favorite webtoon:', error);
    return { success: false, message: error.message };
  }
};

// 관심 웹툰 삭제
export const removeFavoriteWebtoon = async (webtoonId) => {
  try {
    const response = await api.delete(`/api/users/favorites/${webtoonId}`, { authRequired: true });
    return { success: true };
  } catch (error) {
    console.error('Error removing favorite webtoon:', error);
    return { success: false, message: error.message };
  }
};


// 관심 웹툰 목록 가져오기
export const getFavoriteWebtoons = async () => {
  try {
    const response = await api.get(`/api/users/favorites`, { authRequired: true });
    return { success: true, data: response.data };
  } catch (error) {
    console.error('Error fetching favorite webtoons:', error);
    return { success: false, message: error.message };
  }
};

// 웹툰이 관심 웹툰인지 확인
export const isFavoriteWebtoon = async (webtoonId) => {
  try {
    const response = await api.get(`/api/users/favorites/${webtoonId}/is-favorite`, { authRequired: true });
    return { success: true, data: response.data };
  } catch (error) {
    console.error('Error checking favorite webtoon:', error);
    return { success: false, message: error.message };
  }
};

