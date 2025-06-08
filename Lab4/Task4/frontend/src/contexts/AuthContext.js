// src/contexts/AuthContext.js
import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
// Мы больше не будем использовать logoutApi отсюда, так как очистка - это дело контекста
import { login as loginApi } from '../services/authService';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(() => localStorage.getItem('jwtToken')); // Загружаем токен при старте
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  // Этот useEffect будет реагировать на изменение токена
  useEffect(() => {
    // Если токен есть, мы пытаемся получить данные пользователя
    const savedUser = localStorage.getItem('iotUser');
    if (token && savedUser) {
      setUser(JSON.parse(savedUser));
    } else {
      setUser(null);
    }
    setIsLoading(false);
  }, [token]); // Зависимость от токена!

  const login = async ({ email, password }) => {
    // loginApi теперь должен вернуть { user, token }
    const { user: userFromApi, token: receivedToken } = await loginApi({ email, password });

    // Сохраняем все, что нужно
    localStorage.setItem('jwtToken', receivedToken);
    localStorage.setItem('iotUser', JSON.stringify(userFromApi));

    // Обновляем состояния
    setToken(receivedToken);
    setUser(userFromApi);
    
    return userFromApi;
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('iotUser');
    navigate('/login', { replace: true });
  };

  const value = {
    user,
    token,
    isAuthenticated: !!token, // <--- ИСТИНА - ЭТО НАЛИЧИЕ ТОКЕНА
    isAdmin: user?.role === 'admin',
    login,
    logout,
    isLoading,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};