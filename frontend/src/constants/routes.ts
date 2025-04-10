export const Routes = {
    HOME: '/',
    LOGIN: '/auth/login',
    SIGNUP: '/auth/signup',
    WEBTOON_NEW: '/webtoon/new',
    WEBTOON_ONGOING: '/webtoon/ongoing',
    WEBTOON_COMPLETED: '/webtoon/completed',
    WEBTOON_DETAIL: (id: string) => `/webtoon/${id}`,
    USER_PROFILE: '/user/profile',
    USER_PROFILE_EDIT: '/user/profile/edit',
    ERROR: '*',
  };