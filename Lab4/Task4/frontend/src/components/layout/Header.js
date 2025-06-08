import React from 'react';
import { AppBar, Toolbar, Typography, Button, IconButton, Menu, MenuItem, Box, Avatar, Tooltip } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { useTranslation } from 'react-i18next';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
// import '../../styles/components/layout/Header.css'; // <-- закоментовано, MUI only

const Header = () => {
  const { t, i18n } = useTranslation();
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  const handleMenu = (event) => setAnchorEl(event.currentTarget);
  const handleClose = () => setAnchorEl(null);
  const handleTitleClick = () => {
    if (isAuthenticated) {
      if (user?.role === 'admin') navigate('/admin');
      else navigate('/user');
    } else {
      navigate('/');
    }
  };
  const changeLanguage = (lng) => i18n.changeLanguage(lng);
  const handleLogout = () => {
    logout();
    handleClose();
    navigate('/login', { replace: true });
  };
  return (
    <AppBar position="fixed" color="primary" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
      <Toolbar>
        <Typography
          variant="h6"
          noWrap
          sx={{ flexGrow: 1, cursor: 'pointer', fontWeight: 700, letterSpacing: 1.5 }}
          onClick={handleTitleClick}
        >
          {t('header.controlPanel', 'IOT Control Panel')}
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <Button
            color={i18n.language === 'en' ? 'secondary' : 'inherit'}
            onClick={() => changeLanguage('en')}
            size="small"
            sx={{ minWidth: 36 }}
            disabled={i18n.language === 'en'}
          >
            EN
          </Button>
          <Button
            color={i18n.language === 'uk' ? 'secondary' : 'inherit'}
            onClick={() => changeLanguage('uk')}
            size="small"
            sx={{ minWidth: 36 }}
            disabled={i18n.language === 'uk'}
          >
            УКР
          </Button>
          {!isAuthenticated ? (
            <Button color="inherit" onClick={() => navigate('/login')}>
              {t('header.login', 'Login')}
            </Button>
          ) : (
            <>
              <Tooltip title={user?.username || ''}>
                <IconButton color="inherit" onClick={handleMenu} size="large">
                  <Avatar sx={{ bgcolor: 'secondary.main', width: 32, height: 32 }}>
                    <AccountCircleIcon />
                  </Avatar>
                </IconButton>
              </Tooltip>
              <Menu
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
              >
                <MenuItem disabled>{user?.username || 'User'}</MenuItem>
                <MenuItem onClick={handleClose}>{t('header.profile', 'Profile')}</MenuItem>
                <MenuItem onClick={handleLogout}>{t('header.logout', 'Logout')}</MenuItem>
              </Menu>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;