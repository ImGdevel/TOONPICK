import React from 'react';
import styles from './SimilarWebtoons.module.css';

const SimilarWebtoons = ({ similarWebtoons }) => {
  return (
    <div className={styles['similar-webtoons']}>
      <h3>비슷한 웹툰</h3>
      <div className={styles['similar-webtoons-list']}>
        {similarWebtoons && similarWebtoons.length > 0 ? (
          similarWebtoons.map((similar) => (
            <div key={similar.id} className={styles['similar-webtoon-item']}>
              {similar.title}
            </div>
          ))
        ) : (
          <div className={styles['no-similar']}>비슷한 웹툰이 없습니다.</div>
        )}
      </div>
    </div>
  );
};

export default SimilarWebtoons; 