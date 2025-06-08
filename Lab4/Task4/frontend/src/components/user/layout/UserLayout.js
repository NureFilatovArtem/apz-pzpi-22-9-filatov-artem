// src/components/user/layout/UserLayout.jsx
import React from 'react';
import { Box, CssBaseline, Toolbar } from '@mui/material';
import { Outlet } from 'react-router-dom';
import UserSidebar from '../UserSideBar'; 
import Header from '../../layout/Header'; // Используем тот же Header, что и в MainLayout

const UserLayout = () => {
  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <Header />
      <UserSidebar />
      <Box
        component="main"
        sx={{ flexGrow: 1, bgcolor: 'background.default', p: 3 }}
      >
        <Toolbar /> {/* Для смещения контента ниже Header'a */}
        <Outlet /> {/* Здесь будут отображаться дочерние страницы */}
      </Box>
    </Box>
  );
};

export default UserLayout;