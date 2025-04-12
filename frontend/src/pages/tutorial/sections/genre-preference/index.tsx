import React, { useState } from 'react';
import styles from './style.module.css';

const GENRES = [
  '로맨스',
  '판타지',
  '액션',
  '일상',
  '스포츠',
  '스릴러',
  '코미디',
  '드라마',
  '무협',
  '학원',
  '미스터리',
  'SF',
];

interface GenrePreferenceFormProps {
  onComplete: (selectedGenres: string[]) => void;
  onSkip: () => void;
}

const GenrePreferenceForm: React.FC<GenrePreferenceFormProps> = ({ onComplete, onSkip }) => {
  const [selectedGenres, setSelectedGenres] = useState<string[]>([]);

  const handleGenreClick = (genre: string) => {
    if (selectedGenres.includes(genre)) {
      setSelectedGenres(selectedGenres.filter((g) => g !== genre));
    } else if (selectedGenres.length < 5) {
      setSelectedGenres([...selectedGenres, genre]);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (selectedGenres.length >= 3) {
      onComplete(selectedGenres);
    }
  };

  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      <div className={styles.genreGrid}>
        {GENRES.map((genre) => (
          <button
            key={genre}
            type="button"
            className={`${styles.genreButton} ${selectedGenres.includes(genre) ? styles.selected : ''}`}
            onClick={() => handleGenreClick(genre)}
          >
            {genre}
          </button>
        ))}
      </div>
      <div className={styles.instructionContainer}>
        <p className={styles.instruction}>
          {selectedGenres.length}/5개 선택 가능 (최소 3개 선택 필요)
        </p>
        {selectedGenres.length < 3 && (
          <p className={styles.warning}>
            최소 3개의 장르를 선택해주세요
          </p>
        )}
      </div>
      <div className={styles.buttonContainer}>
        <button
          type="button"
          className={styles.skipButton}
          onClick={onSkip}
        >
          건너뛰기
        </button>
        <button
          type="submit"
          className={`${styles.submitButton} ${selectedGenres.length < 3 ? styles.disabled : ''}`}
          disabled={selectedGenres.length < 3}
        >
          다음
        </button>
      </div>
    </form>
  );
};

export default GenrePreferenceForm; 