import React, { useState } from 'react';
import styles from './SearchAndFilter.module.css';

interface SearchAndFilterProps {
  onSearch: (query: string) => void;
  onFilterChange: (filters: { genres: string[]; platforms: string[] }) => void;
}

const genres = ['로맨스', '판타지', '액션', '드라마', '코미디', '스포츠', '스릴러', '호러'];
const platforms = ['네이버', '카카오', '탑툰', '레진코믹스', '투믹스'];

const SearchAndFilter: React.FC<SearchAndFilterProps> = ({ onSearch, onFilterChange }) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedGenres, setSelectedGenres] = useState<string[]>([]);
  const [selectedPlatforms, setSelectedPlatforms] = useState<string[]>([]);

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    const query = e.target.value;
    setSearchQuery(query);
    onSearch(query);
  };

  const handleGenreToggle = (genre: string) => {
    const newGenres = selectedGenres.includes(genre)
      ? selectedGenres.filter(g => g !== genre)
      : [...selectedGenres, genre];
    setSelectedGenres(newGenres);
    onFilterChange({ genres: newGenres, platforms: selectedPlatforms });
  };

  const handlePlatformToggle = (platform: string) => {
    const newPlatforms = selectedPlatforms.includes(platform)
      ? selectedPlatforms.filter(p => p !== platform)
      : [...selectedPlatforms, platform];
    setSelectedPlatforms(newPlatforms);
    onFilterChange({ genres: selectedGenres, platforms: newPlatforms });
  };

  return (
    <div className={styles.container}>
      <div className={styles.searchContainer}>
        <input
          type="text"
          value={searchQuery}
          onChange={handleSearch}
          placeholder="웹툰 제목으로 검색"
          className={styles.searchInput}
        />
      </div>

      <div className={styles.filterContainer}>
        <div className={styles.filterSection}>
          <h3 className={styles.filterTitle}>장르</h3>
          <div className={styles.filterTags}>
            {genres.map(genre => (
              <button
                key={genre}
                className={`${styles.filterTag} ${selectedGenres.includes(genre) ? styles.selected : ''}`}
                onClick={() => handleGenreToggle(genre)}
              >
                {genre}
              </button>
            ))}
          </div>
        </div>

        <div className={styles.filterSection}>
          <h3 className={styles.filterTitle}>플랫폼</h3>
          <div className={styles.filterTags}>
            {platforms.map(platform => (
              <button
                key={platform}
                className={`${styles.filterTag} ${selectedPlatforms.includes(platform) ? styles.selected : ''}`}
                onClick={() => handlePlatformToggle(platform)}
              >
                {platform}
              </button>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SearchAndFilter; 