// src/services/webtoonService.js
const WEBTOONS = [
    { id: 1, title: '웹툰 1', author: '작가 1', rating: 4.5, imageUrl: '/images/webtoon1.jpg' },
    { id: 2, title: '웹툰 2', author: '작가 2', rating: 4.0, imageUrl: '/images/webtoon2.jpg' },
    { id: 3, title: '웹툰 3', author: '작가 3', rating: 3.5, imageUrl: '/images/webtoon3.jpg' },
    // 더 많은 웹툰 데이터...
  ];
  
// src/services/webtoonService.js
export const getWebtoons = async () => {
  // API 호출 로직 (모든 웹툰 데이터를 불러오는 부분)
  // 여기에 실제 API 호출 코드가 들어갈 수 있습니다.
  return [
    { id: 1, title: '웹툰1', status: '연재중', day: '월', genres: ['판타지', '액션'] },
    { id: 2, title: '웹툰2', status: '완결', day: '화', genres: ['드라마', '일상'] },
    // ...더 많은 데이터
  ];
};

export const getCarouselImages = async () => {
  // API 호출 로직 (캐러셀 이미지를 불러오는 부분)
  return [
    { id: 1, imageUrl: 'https://via.placeholder.com/800x400', title: 'Banner 1' },
    { id: 2, imageUrl: 'https://via.placeholder.com/800x400', title: 'Banner 2' },
    // ...더 많은 데이터
  ];
};

// 요일에 맞춰 웹툰 데이터를 불러옴
export const getWebtoonByDayOfWeek = async (dayOfWeek) => {
  try {
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


export const getWebtoonById = async (id) => {
  // 특정 웹툰 데이터를 불러오는 로직
  const allWebtoons = await getWebtoons();
  return allWebtoons.find((webtoon) => webtoon.id === id);
};

// 오류 수정: getFilteredWebtoons 함수 내보내기 추가
export const getFilteredWebtoons = async ({ statusFilter, dayFilter, genreFilter, page }) => {
  const allWebtoons = await getWebtoons();

  // 필터 적용 로직
  const filteredWebtoons = allWebtoons.filter((webtoon) => {
    return (
      (statusFilter === '전체' || webtoon.status === statusFilter) &&
      (dayFilter === '전체' || webtoon.day === dayFilter) &&
      (genreFilter.length === 0 || genreFilter.some((genre) => webtoon.genres.includes(genre)))
    );
  });

  // 페이지네이션 로직
  const startIndex = (page - 1) * 15;
  const endIndex = startIndex + 15;

  return filteredWebtoons.slice(startIndex, endIndex);
};