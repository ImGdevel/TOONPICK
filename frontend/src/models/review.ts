export interface Review {
  id: number;
  webtoonId: number;
  memberId: {
    username: string;
    nickname: string;
    profilePicture: string;
  };
  rating: number;
  comment: string;
  likes: number;
  createdDate: string;
  modifiedDate: string;
}

export interface ReviewRequest {
  webtoonId: number;
  rating: number;
  comment: string;
} 