export const Routes = {
    HOME: '/',
    LOGIN: '/auth/login',
    SIGNUP: '/auth/signup',
    LOGIN_CALLBACK: '/auth/login-collback',
    WEBTOON_NEW: '/webtoon/new',
    WEBTOON_ONGOING: '/webtoon/ongoing',
    WEBTOON_COMPLETED: '/webtoon/completed',
    WEBTOON_DETAIL: (id: string) => `/webtoon/${id}`,
    WEBTOON_RATING_LIST: '/webtoon/rating-list',
    USER_PROFILE: '/user/profile',
    USER_PROFILE_EDIT: '/user/profile/edit',
    ERROR: '*',
  };