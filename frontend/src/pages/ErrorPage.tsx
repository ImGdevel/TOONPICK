import { useRouteError, isRouteErrorResponse } from 'react-router-dom';
import styles from './ErrorPage.module.css';

const ErrorPage: React.FC = () => {
  const error = useRouteError();
  
  let errorMessage: string;
  if (isRouteErrorResponse(error)) {
    errorMessage = error.statusText || error.data?.message || '페이지를 찾을 수 없습니다.';
  } else if (error instanceof Error) {
    errorMessage = error.message;
  } else {
    errorMessage = '알 수 없는 오류가 발생했습니다.';
  }

  return (
    <div className={styles.errorPage}>
      <h1>오류가 발생했습니다</h1>
      <p>{errorMessage}</p>
    </div>
  );
};

export default ErrorPage; 