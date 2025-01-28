import React, { useState } from 'react';
import styles from './ProfilePictureUpload.module.css';

const ProfilePictureUpload: React.FC<{ onSave: (file: File | null) => void }> = ({ onSave }) => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files ? event.target.files[0] : null;
    setSelectedFile(file);
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewUrl(reader.result as string);
      };
      reader.readAsDataURL(file);
    } else {
      setPreviewUrl(null);
    }
  };

  const handleSave = () => {
    if (selectedFile) {
      onSave(selectedFile);
    }
  };

  return (
    <div className={styles.uploadContainer}>
      <h2>프로필 사진 등록/수정</h2>
      <div className={styles.preview}>
        {previewUrl ? (
          <img src={previewUrl} alt="미리보기" className={styles.previewImage} />
        ) : (
          <div className={styles.placeholder}>미리보기 없음</div>
        )}
      </div>
      <label className={styles.fileInputLabel}>
        파일 선택:
        <input
          type="file"
          accept="image/*"
          onChange={handleFileChange}
          className={styles.fileInput}
        />
      </label>
      <button onClick={handleSave} className={styles.saveButton}>
        등록
      </button>
    </div>
  );
};

export default ProfilePictureUpload; 