// src/pages/Admin/DataManagementPage.jsx

import React, { useState, useEffect, useMemo, useCallback } from 'react';
import {
  Box, Typography, Paper, FormGroup, FormControlLabel, Checkbox, TextField, CircularProgress, Alert, Table,
  TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton, Collapse, Grid, Button,
  Dialog, DialogTitle, DialogContent, DialogActions
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import SearchIcon from '@mui/icons-material/Search';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import { dataFetchers, dataCreators } from '../../services/dataService'; // Импортируем и Creators

// --- РАСШИРЕННАЯ КОНФИГУРАЦИЯ ТАБЛИЦ С ПОЛЯМИ ДЛЯ ФОРМЫ ---
const tableConfigs = {
  buildings: { 
    label: 'Buildings', 
    columns: ['id', 'name', 'address', 'office_id'],
    formFields: [
      { name: 'name', label: 'Building Name', type: 'text', required: true },
      { name: 'address', label: 'Address', type: 'text', required: true },
      { name: 'office_id', label: 'Office ID', type: 'number', required: true },
    ]
  },
  sensors: { 
    label: 'Sensors', 
    columns: ['id', 'type', 'building_id'],
    formFields: [
        { name: 'type', label: 'Sensor Type', type: 'text', required: true },
        { name: 'building_id', label: 'Building ID', type: 'number', required: true },
    ]
  },
  offices: {
    label: 'Offices', 
    columns: ['id', 'name', 'location'],
    formFields: [
      { name: 'name', label: 'Office Name', type: 'text', required: true },
      { name: 'location', label: 'Location', type: 'text', required: true },
    ]
  },
  // Для измерений и подписок пока не делаем добавление, т.к. они сложнее
  measurements: { label: 'Measurements', columns: ['id', 'sensor_id', 'value', 'timestamp'], formFields: [] },
  subscriptions: { label: 'Subscriptions', columns: ['id', 'user_id', 'sensor_id', 'notification_type'], formFields: [] },
};


// --- КОМПОНЕНТ ДИНАМИЧЕСКОЙ ФОРМЫ В МОДАЛЬНОМ ОКНЕ ---
const DynamicFormDialog = ({ open, onClose, onSubmit, title, fields, loading }) => {
  const [formData, setFormData] = useState({});

  useEffect(() => {
    // Сбрасываем форму при открытии
    if (open) {
      const initialFormState = fields.reduce((acc, field) => {
        acc[field.name] = '';
        return acc;
      }, {});
      setFormData(initialFormState);
    }
  }, [open, fields]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };
  
  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{title}</DialogTitle>
      <Box component="form" onSubmit={handleSubmit}>
        <DialogContent>
          {fields.map(field => (
            <TextField
              key={field.name}
              margin="dense"
              name={field.name}
              label={field.label}
              type={field.type || 'text'}
              required={field.required}
              fullWidth
              variant="outlined"
              value={formData[field.name] || ''}
              onChange={handleChange}
            />
          ))}
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose}>Cancel</Button>
          <Button type="submit" variant="contained" disabled={loading}>
            {loading ? <CircularProgress size={24}/> : "Create"}
          </Button>
        </DialogActions>
      </Box>
    </Dialog>
  );
}


// --- КОМПОНЕНТ УНИВЕРСАЛЬНОЙ ТАБЛИЦЫ (С КНОПКОЙ ADD) ---
const DynamicDataTable = ({ title, tableKey, data, columns, loading, error, onAdd, onEdit, onDelete }) => {
  return (
    <Box sx={{ mb: 4 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 1 }}>
            <Typography variant="h5" gutterBottom component="div" sx={{ mb: 0 }}>
              {title}
            </Typography>
            {/* Кнопка ADD теперь здесь! Она не отображается для таблиц, где нет полей формы */}
            {tableConfigs[tableKey].formFields.length > 0 && (
              <Button size="small" variant="outlined" startIcon={<AddIcon />} onClick={() => onAdd(tableKey)}>
                Add New
              </Button>
            )}
        </Box>
        {loading ? <Box sx={{ display: 'flex', justifyContent: 'center', my: 4 }}><CircularProgress /></Box> :
        error ? <Alert severity="error">{error}</Alert> :
        (
            <TableContainer component={Paper}>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            {columns.map(col => <TableCell key={col} sx={{ fontWeight: 'bold' }}>{col.toUpperCase()}</TableCell>)}
                            <TableCell align="right" sx={{ fontWeight: 'bold' }}>ACTIONS</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map((row) => (
                            <TableRow key={row.id} hover>
                                {columns.map(col => <TableCell key={`${row.id}-${col}`}>{row[col] === null ? 'NULL' : String(row[col])}</TableCell>)}
                                <TableCell align="right">
                                    <IconButton size="small" color="primary" onClick={() => onEdit(tableKey, row)}><EditIcon fontSize="inherit" /></IconButton>
                                    <IconButton size="small" color="error" onClick={() => onDelete(tableKey, row)}><DeleteIcon fontSize="inherit" /></IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                        {data.length === 0 && <TableRow><TableCell colSpan={columns.length + 1} align="center">No data available.</TableCell></TableRow>}
                    </TableBody>
                </Table>
            </TableContainer>
        )}
    </Box>
  );
};


const DataManagementPage = () => {
  const { t } = useTranslation();
  const [selectedTables, setSelectedTables] = useState({});
  const [searchTerm, setSearchTerm] = useState('');
  
  const [data, setData] = useState({});
  const [loading, setLoading] = useState({});
  const [errors, setErrors] = useState({});
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalConfig, setModalConfig] = useState(null);
  const [formLoading, setFormLoading] = useState(false);
  
  const fetchData = useCallback((key) => {
    setLoading(prev => ({ ...prev, [key]: true }));
    dataFetchers[key]()
      .then(response => setData(prev => ({ ...prev, [key]: response.data })))
      .catch(err => setErrors(prev => ({ ...prev, [key]: err?.response?.data?.message || `Failed to load ${key}` })))
      .finally(() => setLoading(prev => ({ ...prev, [key]: false })));
  }, []);

  useEffect(() => {
    Object.keys(selectedTables).forEach(key => {
      if (selectedTables[key]) {
        fetchData(key);
      }
    });
  }, [selectedTables, fetchData]);

  // --- ИСПРАВЛЕНА ЛОГИКА ПОИСКА ---
  const filteredData = useMemo(() => {
    if (!searchTerm) return data;
    const lowercasedFilter = searchTerm.toLowerCase();
    
    const filtered = {};
    Object.keys(data).forEach(key => {
      if (data[key]) {
        filtered[key] = data[key].filter(item => 
          Object.values(item).some(val => 
            // Точное совпадение вместо 'includes' для более релевантного поиска
            String(val).toLowerCase() === lowercasedFilter
          )
        );
      }
    });
    return filtered;
  }, [searchTerm, data]);
  
  const handleOpenAddModal = (tableKey) => {
    setModalConfig({
      key: tableKey,
      title: `Add New ${tableConfigs[tableKey].label}`,
      fields: tableConfigs[tableKey].formFields,
    });
    setIsModalOpen(true);
  };
  
  const handleFormSubmit = async (formData) => {
    setFormLoading(true);
    try {
        const createFn = dataCreators[modalConfig.key];
        await createFn(formData);
        setIsModalOpen(false);
        fetchData(modalConfig.key); // Перезагружаем данные для обновленной таблицы
    } catch(err) {
        alert(err?.response?.data?.message || 'Failed to create record.');
    } finally {
        setFormLoading(false);
    }
  };

  const handleCheckboxChange = (event) => setSelectedTables({ ...selectedTables, [event.target.name]: event.target.checked });
  
  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>{t('dataManagement.title', 'Data Management')}</Typography>
      
      <Paper sx={{ p: 2, mb: 4, borderRadius: 2 }} elevation={2}>
        {/* Панель управления с чекбоксами и поиском */}
        <FormGroup row>{Object.keys(tableConfigs).map((key) => <FormControlLabel key={key} control={<Checkbox checked={!!selectedTables[key]} onChange={handleCheckboxChange} name={key} />} label={tableConfigs[key].label} />)}</FormGroup>
        <TextField fullWidth variant="outlined" margin="normal" size="small" placeholder="Search for an exact value..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} InputProps={{ startAdornment: <SearchIcon color="action" sx={{ mr: 1 }} /> }}/>
      </Paper>
      
      {/* Отображение таблиц */}
      <Box>{Object.keys(tableConfigs).map((key) => (
          <Collapse in={!!selectedTables[key]} key={key}>
            <DynamicDataTable
              title={tableConfigs[key].label}
              tableKey={key}
              data={filteredData[key] || []}
              columns={tableConfigs[key].columns}
              loading={loading[key]}
              error={errors[key]}
              onAdd={handleOpenAddModal}
              onEdit={(table, item) => alert(`Edit ${table} - item ${item.id}`)} // Placeholder
              onDelete={(table, item) => alert(`Delete ${table} - item ${item.id}`)} // Placeholder
            />
          </Collapse>
        ))}
      </Box>

      {/* Модальное окно */}
      {modalConfig && (
        <DynamicFormDialog
            open={isModalOpen}
            onClose={() => setIsModalOpen(false)}
            onSubmit={handleFormSubmit}
            title={modalConfig.title}
            fields={modalConfig.fields}
            loading={formLoading}
        />
      )}
    </Box>
  );
};

export default DataManagementPage;