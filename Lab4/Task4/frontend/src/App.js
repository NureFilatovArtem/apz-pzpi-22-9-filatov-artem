// src/App.js
import React from 'react';
import AppRouter from './routes/AppRouter';
// Глобальні стилі та контексти вже мають бути підключені в index.js

function App() {
  return (
    // Цей div "App" може бути навіть зайвим,
    // оскільки MainLayout і глобальні стилі вже керують загальним виглядом.
    // Але поки можна залишити, якщо немає конфліктів.
    <div className="App">
      <AppRouter />
    </div>
  );
}

export default App;