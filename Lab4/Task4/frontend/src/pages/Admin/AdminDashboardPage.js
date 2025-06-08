// src/pages/Admin/AdminDashboardPage.js
import React from 'react';
import { useTranslation } from 'react-i18next';

const AdminDashboardPage = () => {
  const { t } = useTranslation();
  // Припустимо, у вас є або будуть ключі для цієї сторінки
  return (
    <div>
      <h1>{t('adminDashboard.title', 'Admin Dashboard')}</h1>
      <p>{t('adminDashboard.welcomeMessage', 'Welcome to the admin control panel!')}</p>
      {/* Сюди буде додаватися контент для адміністратора */}
    </div>
  );
};

export default AdminDashboardPage;