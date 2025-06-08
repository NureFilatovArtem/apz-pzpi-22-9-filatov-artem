// src/pages/UserDataViewPage.jsx
import React, { useState, useEffect, useMemo } from 'react';
import {
  Box, Typography, Paper, Grid, FormControl, InputLabel, Select, MenuItem,
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, CircularProgress, Alert
} from '@mui/material';
import { fetchBuildings, fetchSensors } from '../services/dataService'; // Используем наш сервис

const UserDataViewPage = () => {
  // Состояния для данных
  const [buildings, setBuildings] = useState([]);
  const [sensors, setSensors] = useState([]);

  // Состояния для фильтров
  const [selectedBuilding, setSelectedBuilding] = useState('');
  
  // Состояния загрузки и ошибок
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadInitialData = async () => {
      setLoading(true);
      setError('');
      try {
        const buildingsRes = await fetchBuildings();
        const sensorsRes = await fetchSensors();
        setBuildings(buildingsRes.data);
        setSensors(sensorsRes.data);
      } catch (err) {
        setError('Failed to load initial data.');
      } finally {
        setLoading(false);
      }
    };
    loadInitialData();
  }, []);

  const filteredSensors = useMemo(() => {
    if (!selectedBuilding) return sensors; // Если здание не выбрано, показываем все сенсоры
    return sensors.filter(sensor => sensor.building_id === selectedBuilding);
  }, [selectedBuilding, sensors]);

  if (loading) return <Box sx={{ display: 'flex', justifyContent: 'center' }}><CircularProgress /></Box>;
  if (error) return <Alert severity="error">{error}</Alert>;

  return (
    <Box>
      <Typography variant="h4" gutterBottom>Data View</Typography>

      <Paper sx={{ p: 2, mb: 3 }}>
        <Typography variant="h6" gutterBottom>Filters</Typography>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth>
              <InputLabel id="building-select-label">Filter by Building</InputLabel>
              <Select
                labelId="building-select-label"
                value={selectedBuilding}
                label="Filter by Building"
                onChange={(e) => setSelectedBuilding(e.target.value)}
              >
                <MenuItem value=""><em>Show All</em></MenuItem>
                {buildings.map(b => <MenuItem key={b.id} value={b.id}>{b.name}</MenuItem>)}
              </Select>
            </FormControl>
          </Grid>
        </Grid>
      </Paper>
      
      <Typography variant="h5" gutterBottom>Sensors</Typography>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Type</TableCell>
              <TableCell>Building ID</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredSensors.map(sensor => (
              <TableRow key={sensor.id}>
                <TableCell>{sensor.id}</TableCell>
                <TableCell>{sensor.type}</TableCell>
                <TableCell>{sensor.building_id}</TableCell>
              </TableRow>
            ))}
            {filteredSensors.length === 0 && <TableRow><TableCell colSpan={3} align="center">No sensors match the criteria.</TableCell></TableRow>}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default UserDataViewPage;