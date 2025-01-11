import Logger from '@utils/Logger';
import { AxiosError } from 'axios';

export const handleErrorResponse = async (error: AxiosError) => {
  if (error.response) {
    const status = error.response.status;
    const message = error.response.data || 'Unknown error';

    switch (status) {
      case 400:
        Logger.error('Bad Request:', { message });
        break;
      case 401:
        Logger.error('Authentication Failed:', { message });
        // todo : Access Token 재발급 요청
        break;
      case 403:
        Logger.error('Access Denied:', { message });
        break;
      case 404:
        Logger.error('Resource Not Found:', { message });
        break;
      default:
        if (status >= 500) {
          Logger.error('Server Error:', { message });
        }
    }
  }

  return Promise.reject(error);
};
