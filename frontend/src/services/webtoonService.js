// src/services/webtoonService.js

// 모든 웹툰 데이터를 불러오는 함수
export const getWebtoons = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/webtoons/series-all');
    if (!response.ok) {
      throw new Error('Failed to fetch webtoons');
    }
    const data = await response.json();
    return { success: true, data: data };
  } catch (error) {
    console.error('Error fetching webtoons:', error);
    return [];
  }
};

// 특정 웹툰 데이터를 불러오는 함수
export const getWebtoonById = async (id) => {
  try {
    const response = await fetch(`http://localhost:8080/api/webtoons/${id}`);
    if (!response.ok) {
      throw new Error('Failed to fetch webtoon by ID');
    }
    const data = await response.json();
    return { success: true, data: data };
  } catch (error) {
    console.error('Error fetching webtoon by ID:', error);
    return null;
  }
};

export const getCompletedWebtoons = async (page) => {
  const size = 60; // 한 번에 가져올 웹툰의 수를 50개로 설정

  try {
    const response = await fetch(`http://localhost:8080/api/webtoons/completed?page=${page}&size=${size}`);
    if (!response.ok) {
      throw new Error('완결된 웹툰을 가져오는 데 실패했습니다.');
    }
    const data = await response.json();
    return { success: true, data: data };
  } catch (error) {
    console.error('완결된 웹툰을 가져오는 중 오류가 발생했습니다:', error);
    return { success: false, data: [] };
  }
};

// 특정 요일의 웹툰 데이터를 불러오는 함수
export const getWebtoonsByDayOfWeek = async (dayOfWeek) => {
  try {
    const response = await fetch(`http://localhost:8080/api/webtoons/series/${dayOfWeek}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch webtoons for day: ${dayOfWeek}`);
    }
    const data = await response.json();
    return { success: true, data: data };
  } catch (error) {
    console.error(`Error fetching webtoons for day: ${dayOfWeek}`, error);
    return { success: false, error: error.message };
  }
};

export const getCarouselImages = async () => {
  // API 호출 로직 (캐러셀 이미지를 불러오는 부분)
  return [
    { id: 1, imageUrl: 'https://via.placeholder.com/800x400', title: 'Banner 1' },
    { id: 2, imageUrl: 'https://via.placeholder.com/800x400', title: 'Banner 2' },
    // ...더 많은 데이터
  ];
};


export const fetchWebtoonsByCategory = async (category) => {
  try {
    const response = await fetch(`/api/webtoons?category=${category}`);
    if (!response.ok) {
      throw new Error('Failed to fetch webtoons');
    }
    return await response.json();
  } catch (error) {
    console.error('Error fetching webtoons:', error);
    return [];
  }
};

// 요일에 맞춰 웹툰 데이터를 불러옴
export const getWebtoonByDayOfWeek = async (dayOfWeek) => {
  try {
    console.log("call");
    const response = await fetch(`http://localhost:8080/api/webtoons/day-of-week/${dayOfWeek}`, {
      method: 'GET',
      credentials: 'include', // 쿠키 포함
    });

    if (response.ok) {
      const webtoons = await response.json(); // 응답 데이터를 JSON으로 변환
      return { success: true, data: webtoons }; // 가져온 데이터를 반환
    } else {
      return { success: false, message: 'Failed to fetch webtoons' };
    }
  } catch (error) {
    return { success: false, message: error.message };
  }
};


