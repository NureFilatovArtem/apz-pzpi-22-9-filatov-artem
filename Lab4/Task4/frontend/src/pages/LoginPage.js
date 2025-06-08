// src/pages/LoginPage.js
import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate, Navigate } from 'react-router-dom';
import { TextField, Button, Box, Alert, CircularProgress, Typography, Paper } from '@mui/material'; // Додав Typography
import { useTranslation } from 'react-i18next'; // <-- ДОДАЙ ЦЕЙ ІМПОРТ

const LoginPage = () => {
  const [email, setEmail] = useState(''); // Правильно, стан для email
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  
  const { login, isAuthenticated, user } = useAuth(); // login тепер з AuthContext
  const navigate = useNavigate();
  const { t } = useTranslation(); // <-- ІНІЦІАЛІЗУЙ useTranslation

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsSubmitting(true);
    try {
      // Тепер передаємо { email, password } з твоїх станів
      const userFromContext = await login({ email, password }); 
      
      if (userFromContext && userFromContext.role === 'admin') {
        navigate('/admin', { replace: true });
      } else if (userFromContext) {
        navigate('/user', { replace: true });
      } else {
        // Цей блок може бути недосяжним, якщо AuthContext.login завжди кидає помилку
        // при невдалому loginApiCall
        setError(t('loginPage.invalidCredentials', 'Invalid credentials (fallback)')); // Додав fallback текст
      }
    } catch (err) {
      // Помилка вже має бути оброблена в AuthContext -> loginApi, але дублюємо тут
      setError(err?.response?.data?.message || err?.message || t('loginPage.invalidCredentials', 'Login failed (fallback)'));
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isAuthenticated) {
    return <Navigate to={user?.role === 'admin' ? '/admin' : '/user'} replace />;
  }

  return (
    <Box 
        display="flex" 
        flexDirection="column" 
        alignItems="center" 
        justifyContent="center" 
        minHeight="80vh" // Або інша висота, щоб центрувати на сторінці
        p={3}
    >
      <Paper elevation={3} sx={{ padding: 4, borderRadius: 2, width: '100%', maxWidth: 400 }}> {/* Додав Paper для рамки */}
        <Typography variant="h4" component="h1" gutterBottom textAlign="center">
          {t('loginPage.title', 'Login')} 
        </Typography>
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}> {/* Використовуємо Box як form */}
          <TextField
            label={t('loginPage.emailLabel', 'Email')} // Змінив usernameLabel на emailLabel або просто Email
            value={email}
            onChange={e => setEmail(e.target.value)}
            fullWidth
            margin="normal"
            autoComplete="email"
            required
            type="email" // Додав тип email для валідації браузером
          />
          <TextField
            label={t('loginPage.passwordLabel', 'Password')}
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            fullWidth
            margin="normal"
            autoComplete="current-password"
            required
          />
          {error && <Alert severity="error" sx={{ mt: 2, mb:1 }}>{error}</Alert>}
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 3, mb: 2, py: 1.5 }} // Збільшив кнопку
            disabled={isSubmitting}
          >
            {isSubmitting ? <CircularProgress size={24} color="inherit"/> : t('loginPage.loginButton', 'Login')}
          </Button>
        </Box>
      </Paper>
    </Box>
  );
};

export default LoginPage;