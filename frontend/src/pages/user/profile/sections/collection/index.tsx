import React from 'react';
import { Webtoon } from '@models/webtoon';
import styles from './style.module.css';

interface CollectionSectionProps {
  collections: {
    id: number;
    name: string;
    thumbnail: string;
    webtoons: Webtoon[];
  }[];
}

const CollectionSection: React.FC<CollectionSectionProps> = ({
  collections,
}) => {
  return (
    <section className={styles.collectionSection}>
      <h2 className={styles.sectionTitle}>나의 컬렉션</h2>
      <div className={styles.collectionGrid}>
        {collections.map((collection) => (
          <div key={collection.id} className={styles.collectionCard}>
            <img
              src={collection.thumbnail}
              alt={collection.name}
              className={styles.collectionThumbnail}
            />
            <div className={styles.collectionInfo}>
              <h3>{collection.name}</h3>
              <span>{collection.webtoons.length}개의 웹툰</span>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
};

export default CollectionSection;
