
export interface Member {
  username: string;
  nickname: string;
  profilePicture: string;
}

export interface BasicMemberInfo {
  username: string;
  nickname: string;
  profilePicture: string;
}


export interface MemberProfile {
  username: string;
  nickname: string;
  role: string;
  profilePicture: string;
  email: string;
  isAdultVerified: boolean;
}