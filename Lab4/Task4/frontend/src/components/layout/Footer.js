// src/components/layout/Footer.js
import React from 'react';
import { useTranslation } from 'react-i18next';
import '../../styles/components/layout/Footer.css'; // <--- ПЕРЕВІРТЕ ЦЕЙ РЯДОК

const Footer = () => {
  const { t } = useTranslation();
  const currentYear = new Date().getFullYear();

  return (
    <footer className="app-footer">
      <p>© {currentYear} {t('footer.appName')}. {t('footer.allRightsReserved')}.</p>
      {/* Можна додати більше інформації або посилань */}
    </footer>
  );
};

export default Footer;