export class ErrorHandler {
  static handleApiError(error: any): { success: false; message: string } {
    console.error('API Error:', error);

    if (error.response) {
      // 서버가 응답을 했지만 상태 코드가 2xx가 아닌 경우
      const status = error.response.status;
      const message = error.response.data?.message || 'Unknown error';

      switch (status) {
        case 400:
          return { success: false, message: '잘못된 요청입니다.' };
        case 401:
          return { success: false, message: '인증이 필요합니다.' };
        case 403:
          return { success: false, message: '접근이 거부되었습니다.' };
        case 404:
          return { success: false, message: '요청한 리소스를 찾을 수 없습니다.' };
        case 500:
          return { success: false, message: '서버 오류가 발생했습니다.' };
        default:
          return { success: false, message };
      }
    } else if (error.request) {
      // 요청이 이루어졌지만 응답을 받지 못한 경우
      return { success: false, message: '서버 응답이 없습니다.' };
    } else {
      // 오류를 발생시킨 요청 설정
      return { success: false, message: error.message || '알 수 없는 오류가 발생했습니다.' };
    }
  }
} 