import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import WebtoonService from '@services/webtoonService';
//import UserService from '@services/UserService';
import WebtoonDetailsSection from '@components/WebtoonDetailsSection';
import AuthService from '@services/AuthService';
import styles from './WebtoonDetailPage.module.css';
import { Webtoon } from '@models/webtoon';

const WebtoonDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [webtoon, setWebtoon] = useState<Webtoon | null>(null);
  //const [isFavorite, setIsFavorite] = useState<boolean>(false);
  //const [isBookmarked, setIsBookmarked] = useState<boolean>(false);

  useEffect(() => {
    const fetchWebtoon = async () => {
      try {
        if (!id) return;
        
        const response = await WebtoonService.getWebtoonById(parseInt(id));
        
        if (response?.success && response.data) {
          setWebtoon(response.data);
          
          if (AuthService.isLoggedIn()) {
            //const favoriteStatus = await UserService.isFavoriteWebtoon(parseInt(id));
            //setIsFavorite(favoriteStatus.data || false);
          }
        }
      } catch (error) {
        console.error('Error fetching webtoon data:', error);
      }
    };

    fetchWebtoon();
  }, [id]);

  /*
  const handleFavoriteClick = async () => {
    try {
      if (!id) return;
      
      if (isFavorite) {
        await UserService.removeFavoriteWebtoon(parseInt(id));
      } else {
        await UserService.addFavoriteWebtoon(parseInt(id));
      }
      setIsFavorite(!isFavorite);
    } catch (error) {
      console.error('Failed to update favorite status:', error);
    }
  };

  const handleBookmarkClick = async () => {
    try {
      setIsBookmarked(!isBookmarked);
    } catch (error) {
      console.error('Failed to update bookmark status:', error);
    }
  };
  */

  if (!webtoon) {
    return <div>로딩중...</div>;
  }

  return (
    <div className={styles.webtoonDetailPage}>
      <WebtoonDetailsSection webtoon={webtoon} />
    </div>
  );
};

export default WebtoonDetailPage;