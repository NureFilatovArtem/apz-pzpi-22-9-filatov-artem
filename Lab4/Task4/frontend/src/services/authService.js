import api from './api';

// credentials: { email, password }
export const login = async (credentials) => {
  try {
    const response = await api.post('/api/auth/login', credentials);
    // НЕ сохраняем токен здесь. Контекст сделает это сам.
    // Просто возвращаем все данные.
    return response.data; // { token, user }
  } catch (error) {
    console.error("Login API error:", error.response ? error.response.data : error.message);
    throw error;
  }
};

export const logout = () => {
  localStorage.removeItem('jwtToken');
};

// (Optional) Получить профиль пользователя
export const fetchUserProfile = async () => {
  const response = await api.get('/me'); // если есть такой endpoint
  return response.data;
}; 