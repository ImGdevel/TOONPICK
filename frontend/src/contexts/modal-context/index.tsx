// contexts/ModalContext.tsx
import React, { createContext, useContext, useState } from 'react';
import LoginRequiredModal from '@components/login-required-modal';

interface ModalContextType {
  showLoginRequiredModal: () => void;
  closeModal: () => void;
}

const ModalContext = createContext<ModalContextType | undefined>(undefined);

export const ModalProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isLoginModalVisible, setLoginModalVisible] = useState(false);

  const showLoginRequiredModal = () => setLoginModalVisible(true);
  const closeModal = () => setLoginModalVisible(false);

  return (
    <ModalContext.Provider value={{ showLoginRequiredModal, closeModal }}>
      {children}
      {isLoginModalVisible && (
        <LoginRequiredModal
          onClose={closeModal}
          onLogin={() => {
            closeModal();
            window.location.href = '/login';
          }}
        />
      )}
    </ModalContext.Provider>
  );
};

export const useModal = (): ModalContextType => {
  const context = useContext(ModalContext);
  if (!context) {
    throw new Error('useModal must be used within a ModalProvider');
  }
  return context;
};
