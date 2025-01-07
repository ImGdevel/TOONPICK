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
  rating: number;
  comment: string;
} 