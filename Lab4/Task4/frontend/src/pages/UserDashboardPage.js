// src/pages/UserDashboardPage.js
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Typography, Box, Paper } from '@mui/material';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';

const UserDashboardPage = () => {
  const { t } = useTranslation();
  return (
    <Paper elevation={3} sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <Typography variant="h4" component="h1" gutterBottom>
        {t('userDashboard.title', 'User Dashboard')}
      </Typography>
      <Box sx={{ display: 'flex', alignItems: 'center', mt: 2, color: 'text.secondary' }}>
        <InfoOutlinedIcon sx={{ mr: 1 }} />
        <Typography variant="body1">
          {t('userDashboard.welcomeMessage', 'Ви можете використовувати бокові кнопочки для того, щоб знайти те, що вам потрібно.')}
        </Typography>
      </Box>
    </Paper>
  );
};

export default UserDashboardPage;