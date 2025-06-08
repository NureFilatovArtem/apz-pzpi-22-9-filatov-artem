// src/routes/AppRouter.js
import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

// Лейауты
import MainLayout from '../components/layout/MainLayout';
import AdminLayout from '../components/admin/layout/AdminLayout';
import UserLayout from '../components/user/layout/UserLayout'; // 1. ДОБАВЛЕН ИМПОРТ

// Сторінки
import LoginPage from '../pages/LoginPage';
import HomePage from '../pages/HomePage';
import UserDashboardPage from '../pages/UserDashboardPage';
import UserDataViewPage from '../pages/UserDataViewPage'; // 2. ДОБАВЛЕН ИМПОРТ
import AdminDashboardPage from '../pages/Admin/AdminDashboardPage';
import UserManagementPage from '../pages/Admin/UserManagementPage';
import DataManagementPage from '../pages/Admin/DataManagementPage';

// Допоміжні компоненти та хуки
import ProtectedRoute from './ProtectedRoute';
import { useAuth } from '../contexts/AuthContext';


const AppRouter = () => {
  const { isAuthenticated, user, isLoading } = useAuth();

  if (isLoading) {
    return <div>App is loading...</div>;
  }

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />

      {/* Публічні роути, наприклад, головна сторінка, огортаються в MainLayout */}
      {/* ЭТОТ БЛОК НЕ ТРОГАЕМ */}
      <Route element={<MainLayout />}>
        <Route path="/" element={<HomePage />} />
      </Route>
      
      {/* --- 3. ИЗМЕНЕНА СТРУКТУРА РОУТОВ ПОЛЬЗОВАТЕЛЯ --- */}
      {/* Теперь все роуты, начинающиеся с /user, будут использовать свой собственный UserLayout */}
      <Route element={<ProtectedRoute allowedRoles={['user', 'admin']} />}>
        <Route path="/user" element={<UserLayout />}>
            {/* Если путь ровно /user, покажется UserDashboardPage */}
            <Route index element={<UserDashboardPage />} />
            {/* Если путь /user/data-view, покажется UserDataViewPage */}
            <Route path="data-view" element={<UserDataViewPage />} />
        </Route>
      </Route>
      
      {/* Захищені роути для адмінів, огортаються в AdminLayout */}
      {/* ЭТОТ БЛОК НЕ ТРОГАЕМ */}
      <Route element={<ProtectedRoute allowedRoles={['admin']} />}>
        <Route element={<AdminLayout />}>
          <Route path="/admin" element={<AdminDashboardPage />} />
          <Route path="/admin/users" element={<UserManagementPage />} />
          <Route path="/admin/data" element={<DataManagementPage />} />
        </Route>
      </Route>

      {/* Fallback роут: перенаправлення на відповідний дашборд, якщо залогінений, або на логін */}
      {/* ЭТОТ БЛОК НЕ ТРОГАЕМ */}
      <Route
        path="*"
        element={
          <Navigate
            to={isAuthenticated ? (user?.role === 'admin' ? '/admin' : '/user') : "/login"}
            replace
          />
        }
      />
    </Routes>
  );
};

export default AppRouter;