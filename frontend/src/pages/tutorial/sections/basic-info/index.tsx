import React, { useState } from 'react';
import styles from './style.module.css';

interface BasicInfoFormProps {
  onComplete: (data: { gender: string; birthYear: number }) => void;
}

const BasicInfoForm: React.FC<BasicInfoFormProps> = ({ onComplete }) => {
  const [gender, setGender] = useState<string>('prefer_not_to_say');
  const [birthYear, setBirthYear] = useState<number>(0);
  const [isValid, setIsValid] = useState<boolean>(false);

  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 100 }, (_, i) => currentYear - i);

  const validateForm = (g: string, year: number) => {
    const isValidYear = year >= currentYear - 100 && year <= currentYear;
    setIsValid(g !== '' && isValidYear);
  };

  const handleGenderChange = (g: string) => {
    setGender(g);
    validateForm(g, birthYear);
  };

  const handleBirthYearChange = (year: number) => {
    setBirthYear(year);
    validateForm(gender, year);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (isValid) {
      onComplete({ gender, birthYear });
    }
  };

  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      <div className={styles.inputGroup}>
        <label className={styles.label}>성별</label>
        <div className={styles.genderButtons}>
          <button
            type="button"
            className={`${styles.genderButton} ${gender === 'prefer_not_to_say' ? styles.selected : ''}`}
            onClick={() => handleGenderChange('prefer_not_to_say')}
          >
            선택 안함
          </button>
          <button
            type="button"
            className={`${styles.genderButton} ${gender === 'male' ? styles.selected : ''}`}
            onClick={() => handleGenderChange('male')}
          >
            남성
          </button>
          <button
            type="button"
            className={`${styles.genderButton} ${gender === 'female' ? styles.selected : ''}`}
            onClick={() => handleGenderChange('female')}
          >
            여성
          </button>
          <button
            type="button"
            className={`${styles.genderButton} ${gender === 'other' ? styles.selected : ''}`}
            onClick={() => handleGenderChange('other')}
          >
            Other
          </button>
        </div>
      </div>

      <div className={styles.inputGroup}>
        <label className={styles.label}>출생 연도</label>
        <div className={styles.yearInputContainer}>
          <input
            type="number"
            value={birthYear || ''}
            onChange={(e) => handleBirthYearChange(Number(e.target.value))}
            min={currentYear - 100}
            max={currentYear}
            className={styles.input}
            placeholder="출생 연도를 입력해주세요"
          />
          <span className={styles.orText}>또는</span>
          <select
            value={birthYear || ''}
            onChange={(e) => handleBirthYearChange(Number(e.target.value))}
            className={styles.select}
          >
            <option value="">연도 선택</option>
            {years.map((year) => (
              <option key={year} value={year}>
                {year}년
              </option>
            ))}
          </select>
        </div>
      </div>

      <button
        type="submit"
        className={`${styles.submitButton} ${!isValid ? styles.disabled : ''}`}
        disabled={!isValid}
      >
        다음
      </button>
    </form>
  );
};

export default BasicInfoForm; 