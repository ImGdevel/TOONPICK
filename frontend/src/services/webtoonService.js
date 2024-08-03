// src/services/webtoonService.js
const WEBTOONS = [
    { id: 1, title: '웹툰 1', author: '작가 1', rating: 4.5, imageUrl: '/images/webtoon1.jpg' },
    { id: 2, title: '웹툰 2', author: '작가 2', rating: 4.0, imageUrl: '/images/webtoon2.jpg' },
    { id: 3, title: '웹툰 3', author: '작가 3', rating: 3.5, imageUrl: '/images/webtoon3.jpg' },
    // 더 많은 웹툰 데이터...
  ];
  
export const getWebtoons = async () => {
  // 더미 데이터 반환
  return Array.from({ length: 15 }, (_, i) => ({
    id: i + 1,
    imageUrl: `https://via.placeholder.com/240x360?text=Webtoon+${i + 1}`,
    title: `Webtoon ${i + 1}`,
    author: `Author ${i + 1}`,
    rating: (Math.random() * 2 + 3).toFixed(1),
  }));
};

export const getCarouselImages = async () => {
  // 더미 데이터 반환
  return [
    'https://via.placeholder.com/900x500?text=Slide+1',
    'https://via.placeholder.com/900x500?text=Slide+2',
    'https://via.placeholder.com/900x500?text=Slide+3',
    'https://via.placeholder.com/900x500?text=Slide+4',
    'https://via.placeholder.com/900x500?text=Slide+5',
  ];
};
  
  export const getWebtoonById = (id) =>
    new Promise((resolve) => {
      const webtoon = WEBTOONS.find((w) => w.id === parseInt(id, 10));
      setTimeout(() => resolve(webtoon), 1000);
    });
  