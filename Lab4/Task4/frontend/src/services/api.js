import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:5000', // Direct backend URL
  withCredentials: false,
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Optionally handle logout or redirect here
      // window.location = '/login';
      // localStorage.removeItem('jwtToken');
    }
    return Promise.reject(error);
  }
);

export default api;