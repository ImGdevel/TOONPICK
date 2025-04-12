import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FiArrowLeft, FiCheck, FiX } from 'react-icons/fi';
import styles from './style.module.css';
import { Routes } from '@constants/routes';

const AdultVerificationPage: React.FC = () => {
  const navigate = useNavigate();
  const [verificationMethod, setVerificationMethod] = useState<'phone' | 'idCard' | null>(null);
  const [phoneNumber, setPhoneNumber] = useState('');
  const [verificationCode, setVerificationCode] = useState('');
  const [isCodeSent, setIsCodeSent] = useState(false);
  const [isVerified, setIsVerified] = useState(false);

  const handleSendCode = () => {
    // TODO: 인증번호 전송 API 연동
    setIsCodeSent(true);
  };

  const handleVerifyCode = () => {
    // TODO: 인증번호 확인 API 연동
    setIsVerified(true);
    setTimeout(() => {
      navigate(Routes.USER_PROFILE_EDIT);
    }, 2000);
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <button className={styles.backButton} onClick={() => navigate(-1)}>
          <FiArrowLeft />
        </button>
        <h1 className={styles.title}>성인 인증</h1>
      </div>

      <div className={styles.content}>
        {!isVerified ? (
          <>
            <div className={styles.methodSelection}>
              <h2 className={styles.sectionTitle}>인증 방법 선택</h2>
              <div className={styles.methodButtons}>
                <button
                  className={`${styles.methodButton} ${verificationMethod === 'phone' ? styles.selected : ''}`}
                  onClick={() => setVerificationMethod('phone')}
                >
                  휴대폰 인증
                </button>
                <button
                  className={`${styles.methodButton} ${verificationMethod === 'idCard' ? styles.selected : ''}`}
                  onClick={() => setVerificationMethod('idCard')}
                >
                  신분증 인증
                </button>
              </div>
            </div>

            {verificationMethod === 'phone' && (
              <div className={styles.phoneVerification}>
                <div className={styles.inputGroup}>
                  <label className={styles.label}>휴대폰 번호</label>
                  <div className={styles.phoneInput}>
                    <input
                      type="tel"
                      value={phoneNumber}
                      onChange={(e) => setPhoneNumber(e.target.value)}
                      placeholder="01012345678"
                      className={styles.input}
                    />
                    <button
                      className={styles.sendButton}
                      onClick={handleSendCode}
                      disabled={!phoneNumber || isCodeSent}
                    >
                      인증번호 전송
                    </button>
                  </div>
                </div>

                {isCodeSent && (
                  <div className={styles.inputGroup}>
                    <label className={styles.label}>인증번호</label>
                    <div className={styles.codeInput}>
                      <input
                        type="text"
                        value={verificationCode}
                        onChange={(e) => setVerificationCode(e.target.value)}
                        placeholder="인증번호 6자리"
                        className={styles.input}
                      />
                      <button
                        className={styles.verifyButton}
                        onClick={handleVerifyCode}
                        disabled={!verificationCode}
                      >
                        확인
                      </button>
                    </div>
                  </div>
                )}
              </div>
            )}

            {verificationMethod === 'idCard' && (
              <div className={styles.idCardVerification}>
                <div className={styles.uploadSection}>
                  <label className={styles.uploadLabel}>
                    신분증 사진 업로드
                    <input type="file" accept="image/*" className={styles.fileInput} />
                  </label>
                  <p className={styles.uploadDescription}>
                    주민등록증, 운전면허증, 여권 중 하나를 선택해주세요.
                  </p>
                </div>
              </div>
            )}
          </>
        ) : (
          <div className={styles.successMessage}>
            <FiCheck className={styles.successIcon} />
            <h2>성인 인증이 완료되었습니다!</h2>
            <p>잠시 후 프로필 페이지로 이동합니다.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default AdultVerificationPage; 