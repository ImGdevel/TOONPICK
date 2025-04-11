export interface Review {
  id: number;
  webtoonId: number;
  userId: string;
  userName: string;
  profilePicture: string;
  rating: number;
  comment: string;
  likes: number;
  createdAt: string;
  modifiedAt: string;
}

export interface ReviewRequest {
  webtoonId: number;
  rating: number;
  comment: string;
}