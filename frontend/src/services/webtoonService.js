// src/services/webtoonService.js
import api from './ApiService';

// 모든 웹툰 데이터
export const getWebtoons = async () => {
  try {
    const response = await api.get('/api/webtoons/series-all', { authRequired: false });
    return { success: true, data: response.data };
  } catch (error) {
    console.error('Error fetching webtoons:', error);
    return [];
  }
};

// 특정 웹툰 데이터
export const getWebtoonById = async (id) => {
  try {
    const response = await api.get(`/api/webtoons/${id}`, { authRequired: false });
    return { success: true, data: response.data };
  } catch (error) {
    console.error('Error fetching webtoon by ID:', error);
    return null;
  }
};

// 완결된 웹툰 데이터
export const getCompletedWebtoons = async (page) => {
  const size = 60;

  try {
    const response = await api.get(`/api/webtoons/completed?page=${page}&size=${size}`, { authRequired: false });
    return { success: true, data: response.data };
  } catch (error) {
    console.error('완결된 웹툰을 가져오는 중 오류가 발생했습니다:', error);
    return { success: false, data: [] };
  }
};


// 특정 요일의 웹툰 데이터
export const getWebtoonsByDayOfWeek = async (dayOfWeek) => {
  try {
    const response = await api.get(`/api/webtoons/series/${dayOfWeek}`, { authRequired: false });
    return { success: true, data: response.data };
  } catch (error) {
    console.error(`Error fetching webtoons for day: ${dayOfWeek}`, error);
    return { success: false, error: error.message };
  }
};

// 
export const getCarouselImages = async () => {
  return [
    { id: 1, imageUrl: 'https://via.placeholder.com/800x400', title: 'Banner 1' },
    { id: 2, imageUrl: 'https://via.placeholder.com/800x400', title: 'Banner 2' },

  ];
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
