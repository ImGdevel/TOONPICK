import React, { useState } from 'react';
import styles from './ReportModal.module.css';

const ReportModal = ({ commentId, onClose }) => {
  const [selectedReason, setSelectedReason] = useState(0);
  const [customReason, setCustomReason] = useState('');

  const handleReasonChange = (e) => {
    setSelectedReason(Number(e.target.value));
    if (Number(e.target.value) !== 7) {
      setCustomReason('');
    }
  };

  const handleCustomReasonChange = (e) => {
    setCustomReason(e.target.value);
  };

  const handleSubmit = () => {
    if (selectedReason === 0) {
      alert('신고사유를 선택해주세요.');
      return;
    }
    if (selectedReason === 7 && !customReason.trim()) {
      alert('기타 사유를 작성해주세요.');
      return;
    }
    
    alert('신고가 완료되었습니다.');
    onClose();
  };

  return (
    <div className={styles['modal-overlay']}>
      <div className={styles['modal-content']}>
        <h2>댓글 신고</h2>
        <div>
          <label htmlFor="reason">신고 사유 선택</label>
          <select id="reason" value={selectedReason} onChange={handleReasonChange}>
            <option value={0}>신고사유를 선택해주세요.</option>
            <option value={1}>선정적이거나 폭력적인 댓글</option>
            <option value={2}>스포일러 성 댓글</option>
            <option value={3}>광고성 댓글</option>
            <option value={4}>욕설/혐오/차별 댓글</option>
            <option value={5}>정치적이거나 불쾌한 표현</option>
            <option value={6}>명예훼손 및 사생활 침해</option>
            <option value={7}>기타</option>
          </select>
        </div>
        
        {selectedReason === 7 && (
          <div>
            <label htmlFor="customReason">기타 사유 입력</label>
            <textarea
              id="customReason"
              value={customReason}
              onChange={handleCustomReasonChange}
              rows={4}
            />
          </div>
        )}
        
        <div className={styles['modal-actions']}>
          <button onClick={handleSubmit}>제출</button>
          <button onClick={onClose}>취소</button>
        </div>
      </div>
    </div>
  );
};

export default ReportModal;
