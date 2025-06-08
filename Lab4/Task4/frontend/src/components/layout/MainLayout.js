// src/components/layout/MainLayout.js
import React from 'react';
import Header from './Header';
import { Outlet } from 'react-router-dom';
import { Box, Toolbar } from '@mui/material';
// import '../../styles/components/layout/MainLayout.css'; // <-- закоментовано, MUI only

const MainLayout = () => (
  <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh', background: '#f4f7f6' }}>
    <Header />
    <Box component="main" sx={{ flexGrow: 1, p: 3, mt: { xs: '56px', sm: '64px' } }}>
      <Toolbar />
      <Outlet />
    </Box>
  </Box>
);

export default MainLayout;