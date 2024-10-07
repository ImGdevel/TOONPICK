// src/services/webtoonReviewService.js
import api from './ApiService';

// 웹툰 리뷰 생성
export const createWebtoonReview = async (webtoonId, rating, comment) => {
  try {
    const reviewData = {
      rating: rating,
      comment: comment
    };

    const response = await api.post(`/api/webtoon/${webtoonId}/reviews`, reviewData, { authRequired: true });

    return { success: true, data: response.data };
  } catch (error) {
    console.error('리뷰 생성 중 오류 발생:', error);
    return { success: false, error: error.message };
  }
};

// 특정 리뷰 가져오기
export const getWebtoonReviewById = async (webtoonId, reviewId) => {
  try {
    const response = await api.get(`/api/webtoon/${webtoonId}/reviews/${reviewId}`);
    return { success: true, data: response.data };
  } catch (error) {
    console.error('리뷰 가져오기 중 오류 발생:', error);
    return { success: false, error: error.message };
  }
};

// 리뷰 수정
export const updateWebtoonReview = async (webtoonId, reviewId, rating, comment) => {
  try {
    const reviewData = {
      rating: rating,
      comment: comment
    };
    
    const response = await api.put(`/api/webtoon/${webtoonId}/reviews/${reviewId}`, reviewData, { authRequired: true });
    return { success: true, data: response.data };
  } catch (error) {
    console.error('리뷰 수정 중 오류 발생:', error);
    return { success: false, error: error.message };
  }
};

// 리뷰 삭제
export const deleteWebtoonReview = async (webtoonId, reviewId) => {
  try {
    await api.delete(`/api/webtoon/${webtoonId}/reviews/${reviewId}`);
    return { success: true };
  } catch (error) {
    console.error('리뷰 삭제 중 오류 발생:', error);
    return { success: false, error: error.message };
  }
};

// 리뷰에 좋아요 토글
export const toggleLikeForReview = async (webtoonId, reviewId) => {
  try {
    const response = await api.post(`/api/webtoon/${webtoonId}/reviews/${reviewId}/like`, null, { authRequired: true });
    return { success: true, data: response.data };
  } catch (error) {
    console.error('리뷰 좋아요 토글 중 오류 발생:', error);
    return { success: false, error: error.message };
  }
};

// 특정 웹툰의 리뷰 목록 가져오기
export const getReviewsByWebtoon = async (webtoonId, sortBy = 'latest', page = 0, size = 20) => {
  try {
    const response = await api.get(`/api/webtoon/${webtoonId}/reviews?sortBy=${sortBy}&page=${page}&size=${size}`);
    return { success: true, data: response.data };
  } catch (error) {
    console.error('웹툰 리뷰 목록 가져오기 중 오류 발생:', error);
    return { success: false, error: error.message };
  }
};

// 특정 웹툰에 대한 사용자 리뷰 가져오기
export const getUserReviewForWebtoon = async (webtoonId) => {
  try {
    const response = await api.get(`/api/webtoon/${webtoonId}/reviews/users`, { authRequired: true });
    return { success: true, data: response.data };
  } catch (error) {
    console.error('사용자 리뷰 가져오기 중 오류 발생:', error);
    return { success: false, error: error.message };
  }
};

// 리뷰 신고
export const reportWebtoonReview = async (webtoonId, reviewId, reportData) => {
  try {
    const response = await api.post(`/api/webtoon/${webtoonId}/reviews/${reviewId}/report`, reportData, { authRequired: true });
    return { success: true, data: response.data };
  } catch (error) {
    console.error('리뷰 신고 중 오류 발생:', error);
    return { success: false, error: error.message };
  }
};