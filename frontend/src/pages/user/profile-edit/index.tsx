import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FiArrowLeft, FiCamera, FiUser, FiAlertCircle, FiEdit2, FiCheck, FiX } from 'react-icons/fi';
import styles from './style.module.css';
import { Routes } from '@constants/routes';
import { MemberProfile } from '@models/member';
import { useAuth } from '@contexts/auth-context';

const ProfileEditPage: React.FC = () => {
  const navigate = useNavigate();
  const { memberProfile, updateProfile } = useAuth();
  const [profileImage, setProfileImage] = useState<string | null>(null);
  const [editingField, setEditingField] = useState<string | null>(null);
  const [tempValue, setTempValue] = useState<string>('');

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (event) => {
        if (event.target?.result) {
          setProfileImage(event.target.result as string);
        }
      };
      reader.readAsDataURL(e.target.files[0]);
    }
  };

  const startEditing = (field: string, currentValue: string) => {
    setEditingField(field);
    setTempValue(currentValue);
  };

  const cancelEditing = () => {
    setEditingField(null);
    setTempValue('');
  };

  const saveEdit = () => {
    if (editingField && memberProfile) {
      const updatedProfile: MemberProfile = {
        ...memberProfile,
        [editingField]: tempValue
      };
      updateProfile(updatedProfile);
      setEditingField(null);
      setTempValue('');
    }
  };

  const handleAdultToggle = () => {
    if (!memberProfile?.adultSettings) return;
    
    if (!memberProfile.adultSettings.adultContentFilter) {
      navigate(Routes.ADULT_VERIFICATION);
    } else {
      const updatedProfile: MemberProfile = {
        ...memberProfile,
        adultSettings: {
          ...memberProfile.adultSettings,
          adultContentFilter: false
        }
      };
      updateProfile(updatedProfile);
    }
  };

  const handleAdultSettingChange = (setting: keyof MemberProfile['adultSettings']) => {
    if (!memberProfile?.adultSettings) return;
    
    const updatedProfile: MemberProfile = {
      ...memberProfile,
      adultSettings: {
        ...memberProfile.adultSettings,
        [setting]: !memberProfile.adultSettings[setting]
      }
    };
    updateProfile(updatedProfile);
  };

  const handleAccountConnect = (provider: keyof MemberProfile['connectedAccounts']) => {
    if (!memberProfile?.connectedAccounts) return;
    
    const updatedProfile: MemberProfile = {
      ...memberProfile,
      connectedAccounts: {
        ...memberProfile.connectedAccounts,
        [provider]: !memberProfile.connectedAccounts[provider]
      }
    };
    updateProfile(updatedProfile);
  };

  if (!memberProfile) {
    return <div>Loading...</div>;
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <button className={styles.backButton} onClick={() => navigate(-1)}>
          <FiArrowLeft />
        </button>
        <h1 className={styles.title}>프로필 수정</h1>
      </div>

      <div className={styles.content}>
        <div className={styles.section}>
          <h2 className={styles.sectionTitle}>기본 정보</h2>
          
          <div className={styles.profileImageContainer}>
            <div className={styles.profileImageWrapper}>
              <img 
                src={profileImage || memberProfile.profilePicture || '/images/profile/user.png'} 
                alt="프로필" 
                style={{ width: '120px', height: '120px', objectFit: 'cover', borderRadius: '50%' }}
              />

              <label className={styles.imageUploadButton}>
                <FiCamera />
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleImageChange}
                  className={styles.imageInput}
                />
              </label>
            </div>
          </div>

          <div className={styles.formGroup}>
            <div className={styles.labelRow}>
              <label className={styles.label}>
                <FiUser />
                닉네임
              </label>
              {editingField === 'nickname' ? (
                <div className={styles.editControls}>
                  <input
                    type="text"
                    value={tempValue}
                    onChange={(e) => setTempValue(e.target.value)}
                    className={styles.editInput}
                  />
                  <button onClick={saveEdit} className={styles.saveButton}>
                    <FiCheck />
                  </button>
                  <button onClick={cancelEditing} className={styles.cancelButton}>
                    <FiX />
                  </button>
                </div>
              ) : (
                <div className={styles.valueRow}>
                  <span className={styles.value}>{memberProfile.nickname}</span>
                  <button 
                    onClick={() => startEditing('nickname', memberProfile.nickname)}
                    className={styles.editButton}
                  >
                    <FiEdit2 />
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>

        <div className={styles.section}>
          <h2 className={styles.sectionTitle}>계정 연동</h2>
          
          <div className={styles.connectedAccounts}>
            <div className={styles.accountItem}>
              <span className={styles.accountName}>Google</span>
              <button
                className={`${styles.connectButton} ${memberProfile.connectedAccounts?.google ? styles.connected : ''}`}
                onClick={() => handleAccountConnect('google')}
              >
                {memberProfile.connectedAccounts?.google ? '연동됨' : '연동하기'}
              </button>
            </div>
            <div className={styles.accountItem}>
              <span className={styles.accountName}>Naver</span>
              <button
                className={`${styles.connectButton} ${memberProfile.connectedAccounts?.naver ? styles.connected : ''}`}
                onClick={() => handleAccountConnect('naver')}
              >
                {memberProfile.connectedAccounts?.naver ? '연동됨' : '연동하기'}
              </button>
            </div>
            <div className={styles.accountItem}>
              <span className={styles.accountName}>Kakao</span>
              <button
                className={`${styles.connectButton} ${memberProfile.connectedAccounts?.kakao ? styles.connected : ''}`}
                onClick={() => handleAccountConnect('kakao')}
              >
                {memberProfile.connectedAccounts?.kakao ? '연동됨' : '연동하기'}
              </button>
            </div>
          </div>
        </div>

        <div className={styles.section}>
          <h2 className={styles.sectionTitle}>보안 설정</h2>
          
          <div className={styles.securityGroup}>
            <div className={styles.securityItem}>
              <span className={styles.securityLabel}>비밀번호 변경</span>
              <button 
                className={styles.securityButton}
                onClick={() => navigate(Routes.PASSWORD_CHANGE)}
              >
                변경하기
              </button>
            </div>
          </div>
        </div>

        <div className={styles.section}>
          <h2 className={styles.sectionTitle}>성인 인증</h2>
          
          <div className={styles.adultVerification}>
            <div className={styles.adultStatus}>
              <span className={styles.adultLabel}>성인 인증 상태</span>
              <div className={styles.toggleContainer}>
                <span className={styles.toggleLabel}>
                  {memberProfile.adultSettings?.adultContentFilter ? '인증됨' : '미인증'}
                </span>
                <label className={styles.toggleSwitch}>
                  <input
                    type="checkbox"
                    checked={memberProfile.adultSettings?.adultContentFilter}
                    onChange={handleAdultToggle}
                  />
                  <span className={styles.toggleSlider} />
                </label>
              </div>
            </div>

            {memberProfile.adultSettings?.adultContentFilter && (
              <div className={styles.adultSettings}>
                <h3 className={styles.settingsTitle}>성인 콘텐츠 설정</h3>
                <div className={styles.settingItem}>
                  <label className={styles.settingLabel}>
                    고어 콘텐츠 필터
                    <input
                      type="checkbox"
                      checked={memberProfile.adultSettings.goreFilter}
                      onChange={() => handleAdultSettingChange('goreFilter')}
                    />
                  </label>
                </div>
                <div className={styles.settingItem}>
                  <label className={styles.settingLabel}>
                    성인 콘텐츠 필터
                    <input
                      type="checkbox"
                      checked={memberProfile.adultSettings.adultContentFilter}
                      onChange={() => handleAdultSettingChange('adultContentFilter')}
                    />
                  </label>
                </div>
                <div className={styles.settingItem}>
                  <label className={styles.settingLabel}>
                    폭력 콘텐츠 필터
                    <input
                      type="checkbox"
                      checked={memberProfile.adultSettings.violenceFilter}
                      onChange={() => handleAdultSettingChange('violenceFilter')}
                    />
                  </label>
                </div>
              </div>
            )}
          </div>
        </div>

        <div className={styles.buttonGroup}>
          <button
            type="button"
            className={styles.notificationButton}
            onClick={() => navigate(Routes.NOTIFICATION_SETTINGS)}
          >
            <FiAlertCircle />
            알림 설정
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProfileEditPage; 