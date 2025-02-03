
export interface Member {
  username: string;
  nickname: string;
  role: string;
  profilePicture: string;
  email: string;
  isAdultVerified: boolean;
}

export interface BasicMemberInfo {
  username: string;
  nickname: string;
  profilePicture: string;
  role: string;
}


export interface MemberProfile {
  username: string;
  nickname: string;
  profilePicture: string;
  level: number;
  points: number;
  bookmarkedWebtoons: number;
  watchedWebtoons: number;
  ratedWebtoons: number;
}