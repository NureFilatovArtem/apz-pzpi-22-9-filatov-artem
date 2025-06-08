import React from 'react';
import Header from '../../layout/Header';
import AdminSidebar from './AdminSidebar';
import { Outlet } from 'react-router-dom';
import { Box, Toolbar } from '@mui/material';

const drawerWidth = 240;

const AdminLayout = () => (
  <Box sx={{ display: 'flex', minHeight: '100vh', background: '#f4f7f6' }}>
    <Header />
    <AdminSidebar />
    <Box
      component="main"
      sx={{
        flexGrow: 1,
        p: 3,
        ml: `${drawerWidth}px`,
        mt: { xs: '56px', sm: '64px' },
        background: '#f4f7f6',
        minHeight: '100vh',
      }}
    >
      <Toolbar />
      <Outlet />
    </Box>
  </Box>
);

export default AdminLayout;