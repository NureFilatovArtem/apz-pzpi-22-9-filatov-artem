import React from 'react';
import { useTranslation } from 'react-i18next';
import { Box, Card, CardContent, Typography } from '@mui/material';
// import '../styles/HomePage.css'; // <-- закоментовано, MUI only

const HomePage = () => {
  const { t } = useTranslation();
  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', mt: 8, mb: 6 }}>
      <Typography variant="h3" align="center" gutterBottom>
        {t('homePage.title')}
      </Typography>
      <Typography variant="h5" align="center" color="text.secondary" sx={{ mb: 4 }}>
        {t('homePage.welcomeText')}
      </Typography>
      <Card sx={{ mt: 4, p: 2, background: 'linear-gradient(90deg, #e0eafc 0%, #cfdef3 100%)', boxShadow: 3 }}>
        <CardContent>
          <Typography variant="h5" align="center" sx={{ mb: 2 }}>
            {t('homePage.sensorStatsTitle', 'Sensor Statistics / Overview')}
          </Typography>
          <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: 180, background: '#f5f7fa', borderRadius: 2, border: '1px dashed #b0bec5' }}>
            <Typography color="text.secondary" align="center">
              {t('homePage.statsPlaceholder', 'IoT Device and Sensor Overview Chart - Coming Soon!')}
            </Typography>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
};
export default HomePage;