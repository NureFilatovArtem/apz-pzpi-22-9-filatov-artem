import api from './api';

// GET /api/users/
export const fetchUsers = async () => {
  try {
    const response = await api.get('/api/users/');
    return response.data;
  } catch (error) {
    console.error('Fetch users API error:', error.response ? error.response.data : error.message);
    throw error;
  }
};

// POST /api/users/
export const createUser = async (userData) => {
  try {
    const response = await api.post('/api/users/', userData);
    return response.data;
  } catch (error) {
    console.error('Create user API error:', error.response ? error.response.data : error.message);
    throw error;
  }
};

// PUT /api/users/:userId
export const updateUser = async (userId, userData) => {
  try {
    const response = await api.put(`/api/users/${userId}`, userData);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// DELETE /api/users/:userId
export const deleteUser = async (userId) => {
  try {
    const response = await api.delete(`/api/users/${userId}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const createAdmin = async (adminData) => {
  // Force role to 'admin'
  return createUser({ ...adminData, role: 'admin' });
}; 