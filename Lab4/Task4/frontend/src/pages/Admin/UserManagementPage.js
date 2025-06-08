import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import {
  Box, Button, IconButton, Table, TableBody, TableCell, TableContainer, TableHead,
  TableRow, Paper, Dialog, DialogTitle, DialogContent, DialogActions, TextField,
  Select, MenuItem, CircularProgress, Snackbar, Alert, Typography
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { fetchUsers, createUser, updateUser, deleteUser } from '../../services/userService';
import { useAuth } from '../../contexts/AuthContext';

const roles = [
  { value: 'admin', label: 'Admin' },
  { value: 'user', label: 'User' },
];

const UserManagementPage = () => {
  const { t } = useTranslation();
  const { user: currentUser } = useAuth(); // Переименовал, чтобы не было конфликта с user в map
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Состояния для модальных окон
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const [selectedUser, setSelectedUser] = useState(null);
  const [form, setForm] = useState({ username: '', email: '', password: '', role: 'user' });
  const [formLoading, setFormLoading] = useState(false);

  useEffect(() => {
    if (currentUser && currentUser.role === 'admin') {
      loadUsers();
    }
    // eslint-disable-next-line
  }, [currentUser]);

  const loadUsers = async () => {
    setLoading(true);
    setError('');
    try {
      const data = await fetchUsers();
      setUsers(data);
    } catch (err) {
      setError(err?.response?.data?.message || err.message || 'Failed to load users');
    } finally {
      setLoading(false);
    }
  };

  const handleOpenAddModal = () => {
    setForm({ username: '', email: '', password: '', role: 'user' }); // Сбрасываем форму
    setIsAddModalOpen(true);
  };
  
  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };
  
  // --- ОБЪЕДИНЕННАЯ ЛОГИКА СОЗДАНИЯ ПОЛЬЗОВАТЕЛЯ ---
  const handleAddUser = async () => {
    setFormLoading(true);
    setError('');
    try {
      await createUser(form); // Используем одну функцию createUser для всех
      setSuccess(t('userManagement.addUserSuccess', 'User added successfully.'));
      setIsAddModalOpen(false);
      loadUsers();
    } catch (err) {
      setError(err?.response?.data?.message || err.message || 'Failed to add user');
    } finally {
      setFormLoading(false);
    }
  };
  
  // Логика редактирования и удаления осталась почти такой же
  const handleOpenEdit = (user) => {
    setSelectedUser(user);
    setForm({ username: user.username, email: user.email, password: '', role: user.role });
    setIsEditModalOpen(true);
  };

  const handleEditUser = async () => {
    setFormLoading(true);
    setError('');
    try {
      await updateUser(selectedUser.id, form);
      setSuccess(t('userManagement.editUserSuccess', 'User updated successfully.'));
      setIsEditModalOpen(false);
      loadUsers();
    } catch (err) {
      setError(err?.response?.data?.message || err.message || 'Failed to update user');
    } finally {
      setFormLoading(false);
    }
  };
  
  const handleOpenDelete = (user) => {
    setSelectedUser(user);
    setIsDeleteModalOpen(true);
  };

  const handleDeleteUser = async () => {
    setFormLoading(true);
    setError('');
    try {
      await deleteUser(selectedUser.id);
      setSuccess(t('userManagement.deleteUserSuccess', 'User deleted successfully.'));
      setIsDeleteModalOpen(false);
      loadUsers();
    } catch (err) {
      setError(err?.response?.data?.message || err.message || 'Failed to delete user');
    } finally {
      setFormLoading(false);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>{t('userManagement.title', 'User Management')}</Typography>
      <Box sx={{ mb: 2 }}>
        {/* --- ОСТАЛАСЬ ТОЛЬКО ОДНА КНОПКА --- */}
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          color="primary"
          onClick={handleOpenAddModal}
        >
          {t('userManagement.addNewUser', 'Add New User')}
        </Button>
      </Box>

      {/* Отображение таблицы и снэкбаров (без изменений) */}
      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}><CircularProgress /></Box>
      ) : error ? (
        <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>
      ) : (
        <TableContainer component={Paper} sx={{ borderRadius: 2, boxShadow: 2 }}>
          {/* Table Head, Body и остальное без изменений */}
          <Table>
            <TableHead sx={{ backgroundColor: '#f8f9fa' }}>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>{t('userManagement.table.username')}</TableCell>
                <TableCell>{t('userManagement.table.email')}</TableCell>
                <TableCell>{t('userManagement.table.role')}</TableCell>
                <TableCell>{t('userManagement.table.actions')}</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user) => (
                <TableRow key={user.id} hover>
                  <TableCell>{user.id}</TableCell>
                  <TableCell>{user.username}</TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>{user.role}</TableCell>
                  <TableCell>
                    <IconButton color="primary" onClick={() => handleOpenEdit(user)}><EditIcon /></IconButton>
                    <IconButton color="error" onClick={() => handleOpenDelete(user)}><DeleteIcon /></IconButton>
                  </TableCell>
                </TableRow>
              ))}
              {users.length === 0 && <TableRow><TableCell colSpan={5} align="center">{t('userManagement.noUsers')}</TableCell></TableRow>}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      {/* --- ЕДИНОЕ МОДАЛЬНОЕ ОКНО ДЛЯ СОЗДАНИЯ --- */}
      <Dialog open={isAddModalOpen} onClose={() => setIsAddModalOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{t('userManagement.addNewUser', 'Add New User')}</DialogTitle>
        <DialogContent>
          <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
            <TextField label={t('userManagement.table.username')} name="username" value={form.username} onChange={handleFormChange} required fullWidth />
            <TextField label={t('userManagement.table.email')} name="email" value={form.email} onChange={handleFormChange} required fullWidth type="email" />
            <TextField label={t('loginPage.passwordLabel')} name="password" value={form.password} onChange={handleFormChange} required fullWidth type="password" />
            <TextField label={t('userManagement.table.role')} name="role" value={form.role} onChange={handleFormChange} select fullWidth>
              {roles.map((role) => <MenuItem key={role.value} value={role.value}>{role.label}</MenuItem>)}
            </TextField>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setIsAddModalOpen(false)}>{t('userManagement.actions.cancel', 'Cancel')}</Button>
          <Button onClick={handleAddUser} variant="contained" disabled={formLoading} color="primary">
            {formLoading ? <CircularProgress size={24} /> : t('userManagement.addUser', 'Add User')}
          </Button>
        </DialogActions>
      </Dialog>
      
      {/* Модальные окна для редактирования и удаления (без изменений) */}
      <Dialog open={isEditModalOpen} onClose={() => setIsEditModalOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{t('userManagement.actions.editUser', 'Edit User')}</DialogTitle>
        <DialogContent>
          <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
            <TextField label={t('userManagement.table.username')} name="username" value={form.username} onChange={handleFormChange} required fullWidth />
            <TextField label={t('userManagement.table.email')} name="email" value={form.email} onChange={handleFormChange} required fullWidth type="email" />
            <TextField label={t('userManagement.table.role')} name="role" value={form.role} onChange={handleFormChange} select fullWidth>
              {roles.map((role) => <MenuItem key={role.value} value={role.value}>{role.label}</MenuItem>)}
            </TextField>
            <TextField label={t('loginPage.passwordLabel')} name="password" value={form.password} onChange={handleFormChange} fullWidth type="password" helperText={t('userManagement.actions.leaveBlank', 'Leave blank to keep current password')} />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setIsEditModalOpen(false)}>{t('userManagement.actions.cancel', 'Cancel')}</Button>
          <Button onClick={handleEditUser} variant="contained" disabled={formLoading}>{formLoading ? <CircularProgress size={24} /> : t('userManagement.actions.updateUser', 'Update User')}</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={isDeleteModalOpen} onClose={() => setIsDeleteModalOpen(false)} maxWidth="xs">
        <DialogTitle>{t('userManagement.actions.deleteUser', 'Delete User')}</DialogTitle>
        <DialogContent><Typography>{t('userManagement.actions.confirmDelete', 'Are you sure you want to delete this user?')}</Typography></DialogContent>
        <DialogActions>
          <Button onClick={() => setIsDeleteModalOpen(false)}>{t('userManagement.actions.cancel', 'Cancel')}</Button>
          <Button onClick={handleDeleteUser} color="error" variant="contained" disabled={formLoading}>{formLoading ? <CircularProgress size={24} /> : t('userManagement.actions.delete', 'Delete')}</Button>
        </DialogActions>
      </Dialog>
      
      {/* Снэкбары */}
      <Snackbar open={!!success} autoHideDuration={3000} onClose={() => setSuccess('')} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}><Alert severity="success">{success}</Alert></Snackbar>
      <Snackbar open={!!error && !loading} autoHideDuration={4000} onClose={() => setError('')} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}><Alert severity="error">{error}</Alert></Snackbar>
    </Box>
  );
};

export default UserManagementPage;