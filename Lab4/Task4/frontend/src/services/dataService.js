// src/services/dataService.js
import api from './api';

// --- Функции для GET-запросов (уже существуют) ---
export const fetchBuildings = () => api.get('/api/buildings');
export const fetchSensors = () => api.get('/api/sensors');
export const fetchOffices = () => api.get('/api/offices');
export const fetchMeasurements = () => api.get('/api/measurements');
export const fetchSubscriptions = () => api.get('/api/subscriptions');

// --- ДОБАВЬТЕ ЭТИ ФУНКЦИИ ДЛЯ POST-ЗАПРОСОВ ---
export const createBuilding = (data) => api.post('/api/buildings', data);
export const createSensor = (data) => api.post('/api/sensors', data);
export const createOffice = (data) => api.post('/api/offices', data);
export const createMeasurement = (data) => api.post('/api/measurements', data);
export const createSubscription = (data) => api.post('/api/subscriptions', data);

// Объект-маппер для удобного вызова GET
export const dataFetchers = {
  buildings: fetchBuildings,
  sensors: fetchSensors,
  offices: fetchOffices,
  measurements: fetchMeasurements,
  subscriptions: fetchSubscriptions,
};

// Объект-маппер для удобного вызова CREATE
export const dataCreators = {
    buildings: createBuilding,
    sensors: createSensor,
    offices: createOffice,
    measurements: createMeasurement,
    subscriptions: createSubscription,
};