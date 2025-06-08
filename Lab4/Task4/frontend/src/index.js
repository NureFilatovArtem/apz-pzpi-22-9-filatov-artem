// src/index.js
import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router } from 'react-router-dom'; // Важливо для useNavigate в AuthContext

import './config/i18n';
import './styles/global.css';
import App from './App';
import { AuthProvider } from './contexts/AuthContext'; // Імпортуємо

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Router> {/* Огортаємо в Router, якщо ще не зроблено */}
      <AuthProvider> {/* Огортаємо в AuthProvider */}
        <React.Suspense fallback={<div>Loading translations...</div>}>
          <App />
        </React.Suspense>
      </AuthProvider>
    </Router>
  </React.StrictMode>
);