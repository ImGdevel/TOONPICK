import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './style.module.css';

import { Webtoon } from '@models/webtoon';
import StarRating from '@components/star-rating';
import StatusBadge from '@components/status-badge';
import WebtoonService from '@services/webtoon-service';
import { AuthContext } from '@contexts/auth-context';
import { useModal } from '@contexts/modal-context';

interface WebtoonRatingCardProps {
  webtoon: Webtoon;
  onClick: () => void;
}

const WebtoonRatingCard: React.FC<WebtoonRatingCardProps> = ({ webtoon, onClick }) => {
  const [rating, setRating] = useState<number>(0);
  const [isHovered, setIsHovered] = useState<boolean>(false);
  const { isLoggedIn } = useContext(AuthContext);
  const { showLoginRequiredModal } = useModal();
  const navigate = useNavigate();

  const handleRatingChange = async (newRating: number) => {
    if (!isLoggedIn) {
      showLoginRequiredModal();
      return;
    }

    try {
      // WebtoonService에 rateWebtoon 메서드가 없으므로 임시로 주석 처리
      // const response = await WebtoonService.rateWebtoon(webtoon.id, newRating);
      // if (response.success) {
      //   setRating(newRating);
      // }
      console.log(`Rating webtoon ${webtoon.id} with ${newRating} stars`);
      setRating(newRating);
    } catch (error) {
      console.error('Error rating webtoon:', error);
    }
  };

  const handleCardClick = (e: React.MouseEvent) => {
    // 별점 클릭 시 상세 페이지로 이동하지 않도록 처리
    if ((e.target as HTMLElement).closest(`.${styles.ratingContainer}`)) {
      e.stopPropagation();
      return;
    }
    onClick();
  };

  return (
    <div 
      className={styles.webtoonCard}
      onClick={handleCardClick}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <div className={styles.thumbnailContainer}>
        <img 
          src={webtoon.thumbnailUrl || 'https://via.placeholder.com/200x280'} 
          alt={webtoon.title} 
          className={styles.thumbnail}
        />
        {webtoon.isAdult && <StatusBadge text="19" />}
        {webtoon.status === 'ONGOING' && <StatusBadge text="연재" />}
      </div>
      
      <div className={styles.infoContainer}>
        <h3 className={styles.title}>{webtoon.title}</h3>
        
        <div className={styles.metaInfo}>
          <span className={styles.author}>
            {webtoon.authors && webtoon.authors.length > 0 
              ? webtoon.authors.map(author => author.name).join(', ') 
              : '작가 정보 없음'}
          </span>
          <span className={styles.rating}>
            {webtoon.averageRating ? `${webtoon.averageRating.toFixed(1)} ⭐` : '평가 없음'}
          </span>
        </div>
        
        <div className={styles.ratingContainer}>
          <StarRating 
            rating={rating} 
            onChange={handleRatingChange} 
            interactive={true}
          />
        </div>
      </div>
    </div>
  );
};

export default WebtoonRatingCard;
