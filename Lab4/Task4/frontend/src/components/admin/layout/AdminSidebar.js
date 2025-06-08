// src/components/admin/layout/AdminSidebar.js
import React from 'react';
import { Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText } from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import DashboardIcon from '@mui/icons-material/Dashboard';
import PeopleIcon from '@mui/icons-material/People';
import StorageIcon from '@mui/icons-material/Storage';
import { NavLink, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
// import '../../../styles/components/layout/AdminSidebar.css'; // <-- закоментовано, MUI only

const drawerWidth = 240;

const AdminSidebar = () => {
  const { t } = useTranslation();
  const location = useLocation();
  const menuItems = [
    { textKey: 'adminSidebar.home', path: '/', icon: <HomeIcon />, end: true },
    { textKey: 'adminSidebar.dashboard', path: '/admin', icon: <DashboardIcon />, end: true },
    { textKey: 'adminSidebar.userManagement', path: '/admin/users', icon: <PeopleIcon /> },
    { textKey: 'adminSidebar.dataManagement', path: '/admin/data', icon: <StorageIcon /> },
  ];
  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        [`& .MuiDrawer-paper`]: {
          width: drawerWidth,
          boxSizing: 'border-box',
          backgroundColor: '#23272f',
          color: '#ecf0f1',
          borderRight: 'none',
        },
        zIndex: (theme) => theme.zIndex.appBar - 1,
      }}
      anchor="left"
    >
      <List sx={{ pt: 1 }}>
        {menuItems.map((item) => (
          <ListItem key={item.textKey} disablePadding>
            <ListItemButton
              component={NavLink}
              to={item.path}
              selected={item.end ? location.pathname === item.path : location.pathname.startsWith(item.path)}
              end={item.end}
              sx={{
                '&.Mui-selected': {
                  backgroundColor: 'rgba(26, 188, 156, 0.18)',
                  borderLeft: '4px solid #1abc9c',
                  '& .MuiListItemIcon-root, & .MuiListItemText-primary': {
                    color: '#1abc9c',
                  },
                },
                '&:hover': {
                  backgroundColor: 'rgba(255,255,255,0.08)',
                },
              }}
            >
              <ListItemIcon sx={{ color: 'inherit', minWidth: 40 }}>{item.icon}</ListItemIcon>
              <ListItemText primary={t(item.textKey)} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Drawer>
  );
};

export default AdminSidebar;