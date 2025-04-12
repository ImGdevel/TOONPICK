import React, { useState } from 'react';
import styles from './style.module.css';

interface ReportModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (reason: string, description: string) => void;
  webtoonId: number;
}

const ReportModal: React.FC<ReportModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
  webtoonId
}) => {
  const [reason, setReason] = useState<string>('');
  const [description, setDescription] = useState<string>('');

  const handleSubmit = (e: React.FormEvent): void => {
    e.preventDefault();
    onSubmit(reason, description);
    setReason('');
    setDescription('');
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modal}>
        <h2>웹툰 신고하기</h2>
        <form onSubmit={handleSubmit}>
          <select
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            required
          >
            <option value="">신고 사유 선택</option>
            <option value="inappropriate">부적절한 콘텐츠</option>
            <option value="copyright">저작권 침해</option>
            <option value="other">기타</option>
          </select>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="상세 내용을 입력해주세요"
            required
          />
          <div className={styles.buttons}>
            <button type="submit" className={styles.submitButton}>신고하기</button>
            <button type="button" onClick={onClose} className={styles.cancelButton}>취소</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ReportModal; 