import { Platform, SerializationStatus, Webtoon, AnalysisData, SimilarWebtoon, MemberProfile } from '@models/webtoon';

export const DUMMY_ANALYSIS: AnalysisData = {
  genreDistribution: [
    { genre: '액션', percentage: 65 },
    { genre: '판타지', percentage: 25 },
    { genre: '모험', percentage: 10 }
  ],
  audienceAge: [
    { age: '10대', percentage: 15 },
    { age: '20대', percentage: 45 },
    { age: '30대', percentage: 30 },
    { age: '40대 이상', percentage: 10 }
  ],
  genderDistribution: [
    { gender: '남성', percentage: 60 },
    { gender: '여성', percentage: 40 }
  ],
  plotComplexity: 6.8,
  characterDevelopment: 7.5,
  worldBuilding: 8.2,
  pacing: 7.0,
  dialogueQuality: 6.5,
  artStyle: 8.0
};

export const DUMMY_RECOMMENDATIONS: SimilarWebtoon[] = [
  {
    id: 1,
    title: '추천 웹툰 1',
    thumbnailUrl: 'https://via.placeholder.com/150x200',
    platform: Platform.NAVER,
    status: SerializationStatus.ONGOING,
    isAdult: false,
    averageRating: 4.5,
    genres: [
      { id: 1, name: '액션' },
      { id: 2, name: '판타지' }
    ]
  },
  {
    id: 2,
    title: '추천 웹툰 2',
    thumbnailUrl: 'https://via.placeholder.com/150x200',
    platform: Platform.KAKAO,
    status: SerializationStatus.COMPLETED,
    isAdult: false,
    averageRating: 4.2,
    genres: [
      { id: 3, name: '로맨스' },
      { id: 4, name: '드라마' }
    ]
  },
  {
    id: 3,
    title: '추천 웹툰 3',
    thumbnailUrl: 'https://via.placeholder.com/150x200',
    platform: Platform.NAVER,
    status: SerializationStatus.ONGOING,
    isAdult: false,
    averageRating: 4.7,
    genres: [
      { id: 5, name: '코미디' },
      { id: 6, name: '일상' }
    ]
  }
];

export const DUMMY_WEBTOON: Webtoon = {
  id: 1,
  title: '테스트 웹툰',
  thumbnailUrl: 'https://via.placeholder.com/300x400',
  platform: Platform.NAVER,
  isAdult: false,
  status: SerializationStatus.ONGOING,
  publishDay: '월요일',
  authors: [
    { id: 1, name: '작가1' },
    { id: 2, name: '작가2' }
  ],
  description: '이것은 테스트 웹툰의 설명입니다. 매우 긴 설명이 들어갈 수 있습니다. 여러 줄의 텍스트가 들어갈 수 있습니다.',
  genres: [
    { id: 1, name: '액션' },
    { id: 2, name: '판타지' }
  ],
  totalRatings: 1000,
  averageRating: 4.5,
  analysisData: DUMMY_ANALYSIS,
  similarWebtoons: DUMMY_RECOMMENDATIONS
};

export const DUMMY_MEMBER_PROFILE: MemberProfile = {
  id: 1,
  username: 'testuser',
  email: 'test@example.com',
  nickname: '테스트유저',
  profileImage: 'https://via.placeholder.com/100x100'
}; 